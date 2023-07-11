package project.htjobboard.controller;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import project.htjobboard.model.Applicant;
import project.htjobboard.model.Company;
import project.htjobboard.model.Job;
import project.htjobboard.repository.ApplicantRepository;
import project.htjobboard.repository.CompanyRepository;
import project.htjobboard.repository.JobRepository;
import project.htjobboard.utils.JwtUtil;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final JwtUtil jwtUtil;

    public CompanyController(CompanyRepository companyRepository, JobRepository jobRepository, JwtUtil jwtUtil) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{id}")
    public Optional<Company> getPosterCompany(@PathVariable("id") String id) {
        List<Company> companies = companyRepository.findAll();

        for (Company company : companies) {
            for (Job job : company.getJobListings()) {
                if (job.getId().equals(id)) {
                    return Optional.of(company);
                }
            }
        }

        return Optional.empty();
    }

    @GetMapping("/current/{id}")
    public Optional<Company> getCurrentCompany(@PathVariable("id") String id) {
        Optional<Company> company = companyRepository.findById(id);
        return company;
    }
    
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> signupRequest) {
        String companyName = signupRequest.get("companyName");
        String email = signupRequest.get("email");
        String password = signupRequest.get("password");
        String passwordConfirm = signupRequest.get("passwordConfirm");

        if (!password.equals(passwordConfirm)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Passwords do not match"));
        }

        // Check if the email is already registered
        if (companyRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email already registered"));
        }

        // Validate input, create a user, etc.
        Company company = createCompany(companyName, email, password);

        companyRepository.save(company);

        // Generate a JWT token for the newly registered user
        String token = generateJwtToken(company);

        // Return the JWT token in the response
        Map<String, String> response = Collections.singletonMap("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<Company> companyOptional = companyRepository.findByEmail(email);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            // Check if password matches
            if (company.getPassword().equals(password)) {
                // Generate JWT token using the company's information
                String token = generateJwtToken(company);
                // Create the response body with the token
                Map<String, String> response = Collections.singletonMap("token", token);
                // Return the response entity with the token
                return ResponseEntity.ok(response);
            } else {
                // Password does not match
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid password"));
            }
        } else {
            // Applicant not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid email"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Company> getCurrentCompany(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            String email = jwtUtil.extractSubject(token);

            

            Optional<Company> optionalCompany = companyRepository.findByEmail(email);
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                return ResponseEntity.ok(company);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/companies")
    public List<Company> getAllCompanies(HttpServletRequest request) {
        
        return companyRepository.findAll();
    }

    @PostMapping("/addJob")
    public Job addJob(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {

        String companyId = requestBody.get("companyId");
        String jobTitle = requestBody.get("jobTitle");
        String salaryStr = requestBody.get("salary");
        String city = requestBody.get("city");
        String jobDescription = requestBody.get("jobDescription");

        double salary = Double.parseDouble(salaryStr);

        Job newJob = new Job();

        newJob.setTitle(jobTitle);
        newJob.setSalary(salary);
        newJob.setLocation(city);
        newJob.setDescription(jobDescription);

        Optional<Company> optionalCompany = companyRepository.findById(companyId);
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();

                // Set the company for the new job
                newJob.setCompany(company);

                // Save the job to the database
                Job savedJob = jobRepository.save(newJob);

                // Update the company's jobListings array
                company.addToJobListings(savedJob);

                // Save the updated company to the database
                companyRepository.save(company);

                // Return the saved job with the generated ID
                return savedJob;
            }

            // Return null or handle the case where the company is not found
            return null;
            }

    @PostMapping("/deleteJob")
    public ResponseEntity<?> deleteJob(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {

        String applicantEmail = requestBody.get("applicantEmail");
        String jobId = requestBody.get("jobId");

        String token = extractToken(request);
        if (token != null) {

            // Retrieve the user from the database based on the email
            Optional<Company> optionalApplicant = companyRepository.findByEmail(applicantEmail);
            if (optionalApplicant.isPresent()) {
                Company company = optionalApplicant.get();

                // Retrieve the job from the database based on the jobId
                Optional<Job> optionalJob = jobRepository.findById(jobId);
                if (optionalJob.isPresent()) {
                    Job job = optionalJob.get();

                    // Remove the job from the wishList array of the user
                    company.removeFromJobListings(job);

                    // Save the updated user in the database
                    companyRepository.save(company);

                    // Return the updated wishList of the user
                    return ResponseEntity.ok(Collections.singletonMap("jobListings", company.getJobListings()));
                }
            }
        }

        // If the user or job is not found, or the token is missing or invalid, return an error response
        return ResponseEntity.badRequest().body("User or job not found");
    }

    @GetMapping("/search")
    public List<Company> searchCompanies(@RequestParam("companyName") String companyName) {
        List<Company> companies = companyRepository.findByNameContainingIgnoreCase(companyName);
        return companies != null ? companies : Collections.emptyList();
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract the token without the "Bearer " prefix
        }
        return null; // Return null if no token is found
    }

    private Company createCompany(String companyName, String email, String password) {
        
        Company company = new Company(companyName, email, password);
        return company;
    }

    private String generateJwtToken(Company company) {
        // Set the expiration time for the token (e.g., 1 day)
        long expirationMillis = 24 * 60 * 60 * 1000; // 1 day in milliseconds

        // Generate the SecretKey from the JWT secret
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        
        String token = Jwts.builder()
                .setSubject(company.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

}
