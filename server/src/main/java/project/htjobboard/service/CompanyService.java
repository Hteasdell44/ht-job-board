package project.htjobboard.service;

import project.htjobboard.model.Company;
import project.htjobboard.model.Job;
import project.htjobboard.repository.CompanyRepository;
import project.htjobboard.repository.JobRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.javafaker.Faker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, JobRepository jobRepository) {
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    public void deleteAllCompanies() {
        companyRepository.deleteAll();
    }

    public void seedCompanies() {
        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("companyData.json");
            String json = new String(inputStream.readAllBytes());

            // Parse the JSON data into a list of Company objects
            ObjectMapper objectMapper = new ObjectMapper();
            List<Company> companies = objectMapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, Company.class));

            // Iterate over the companies and save them to the database
            for (Company company : companies) {
                // Save the company to the database
                Company savedCompany = companyRepository.save(company);

                // Generate or add jobs for the company
                List<Job> jobs = generateJobsForCompany(savedCompany);

                // Update the jobListings property of the company
                savedCompany.setJobListings(jobs.toArray(new Job[0]));

                // Save the updated company with the associated jobs to the database
                companyRepository.save(savedCompany);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Job> generateJobsForCompany(Company company) {
        List<Job> jobs = new ArrayList<>();

        // Generate or add jobs for the company
        Faker faker = new Faker(new Locale("en-US"));
        for (int i = 0; i < 5; i++) {
            String title = faker.job().title();
            double salary = faker.number().randomDouble(2, 50000, 150000);
            String location = faker.address().city();

            Job job = new Job(title, salary, location);
            job.setCompany(company);

            Job savedJob = jobRepository.save(job);
            jobs.add(savedJob);
        }

        System.out.println("Jobs generated and assigned to company: " + company.getName());

        return jobs;
    }
}

