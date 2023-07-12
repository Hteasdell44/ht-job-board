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
import project.htjobboard.model.Job;
import project.htjobboard.repository.ApplicantRepository;
import project.htjobboard.repository.JobRepository;
import project.htjobboard.utils.JwtUtil;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/applicant")
public class ApplicantController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final ApplicantRepository applicantRepository;
    private final JobRepository jobRepository;
    private final JwtUtil jwtUtil;

    public ApplicantController(ApplicantRepository applicantRepository, JobRepository jobRepository, JwtUtil jwtUtil) {
        this.applicantRepository = applicantRepository;
        this.jobRepository = jobRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public List<Applicant> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Applicant> getSpecificApplicant(@PathVariable("id") String id) {
        return applicantRepository.findById(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> signupRequest) {
    String firstName = signupRequest.get("firstName");
    String lastName = signupRequest.get("lastName");
    String email = signupRequest.get("email");
    String password = signupRequest.get("password");
    String passwordConfirm = signupRequest.get("passwordConfirm");

    if (!password.equals(passwordConfirm)) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Passwords do not match"));
    }

    // Check if the email is already registered
    if (applicantRepository.existsByEmail(email)) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email already registered"));
    }

    // Validate input, create a user, etc.
    Applicant applicant = createApplicant(firstName, lastName, email, password);

    applicantRepository.save(applicant);

    // Generate a JWT token for the newly registered user
    String token = generateJwtToken(applicant);

    // Return the JWT token in the response
    Map<String, String> response = Collections.singletonMap("token", token);
    return ResponseEntity.ok(response);
}

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Optional<Applicant> applicantOptional = applicantRepository.findByEmail(email);

        if (applicantOptional.isPresent()) {
            Applicant applicant = applicantOptional.get();
            // Check if password matches
            if (applicant.getPassword().equals(password)) {
                // Generate JWT token using the applicant's information
                String token = generateJwtToken(applicant);
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
    public ResponseEntity<Applicant> getCurrentApplicant(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            String email = jwtUtil.extractSubject(token);

            

            Optional<Applicant> optionalApplicant = applicantRepository.findByEmail(email);
            if (optionalApplicant.isPresent()) {
                Applicant applicant = optionalApplicant.get();
                return ResponseEntity.ok(applicant);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addToWishList")
    public ResponseEntity<?> addToWishList(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {

        String applicantEmail = requestBody.get("applicantEmail");
        String jobId = requestBody.get("jobId");

        String token = extractToken(request);
        if (token != null) {

            // Retrieve the user from the database based on the email
            Optional<Applicant> optionalApplicant = applicantRepository.findByEmail(applicantEmail);
            if (optionalApplicant.isPresent()) {
                Applicant applicant = optionalApplicant.get();

                // Retrieve the job from the database based on the jobId
                Optional<Job> optionalJob = jobRepository.findById(jobId);
                if (optionalJob.isPresent()) {
                    Job job = optionalJob.get();

                    // Add the job to the wishList array of the user
                    applicant.addToWishList(job);

                    // Save the updated user in the database
                    applicantRepository.save(applicant);

                    // Return the updated wishList of the user
                    return ResponseEntity.ok(Collections.singletonMap("wishList", applicant.getWishList()));
                }
            }
        }

        // If the user or job is not found, or the token is missing or invalid, return an error response
        return ResponseEntity.badRequest().body("User or job not found");
    }

    @PostMapping("/removeFromWishList")
    public ResponseEntity<?> removeFromWishList(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {

        String applicantEmail = requestBody.get("applicantEmail");
        String jobId = requestBody.get("jobId");

        String token = extractToken(request);
        if (token != null) {

            // Retrieve the user from the database based on the email
            Optional<Applicant> optionalApplicant = applicantRepository.findByEmail(applicantEmail);
            if (optionalApplicant.isPresent()) {
                Applicant applicant = optionalApplicant.get();

                // Retrieve the job from the database based on the jobId
                Optional<Job> optionalJob = jobRepository.findById(jobId);
                if (optionalJob.isPresent()) {
                    Job job = optionalJob.get();

                    // Remove the job from the wishList array of the user
                    applicant.removeFromWishList(job);

                    // Save the updated user in the database
                    applicantRepository.save(applicant);

                    // Return the updated wishList of the user
                    return ResponseEntity.ok(Collections.singletonMap("wishList", applicant.getWishList()));
                }
            }
        }

        // If the user or job is not found, or the token is missing or invalid, return an error response
        return ResponseEntity.badRequest().body("User or job not found");
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract the token without the "Bearer " prefix
        }
        return null; // Return null if no token is found
    }

    private Applicant createApplicant(String firstName, String lastName, String email, String password) {
        
        Applicant applicant = new Applicant(firstName, lastName, email, password);
        return applicant;
    }

    private String generateJwtToken(Applicant applicant) {
        // Set the expiration time for the token (e.g., 1 day)
        long expirationMillis = 24 * 60 * 60 * 1000; // 1 day in milliseconds

        // Generate the SecretKey from the JWT secret
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        // Generate the JWT token with the applicant's information
        String token = Jwts.builder()
                .setSubject(applicant.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

   
}
