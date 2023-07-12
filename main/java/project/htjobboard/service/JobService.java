package project.htjobboard.service;

import project.htjobboard.model.Job;
import project.htjobboard.repository.JobRepository;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {
    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void deleteAllJobs() {
        jobRepository.deleteAll();
    }

    public void createDummyJob() {
        Job job = new Job();
        job.setTitle("Software Engineer");
        job.setSalary(100000.0);
        job.setLocation("City Name");

        jobRepository.save(job);
    }

    public void seedJobs() {
        Faker faker = new Faker();
        List<Job> jobs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Job job = new Job();
            job.setTitle(faker.job().title());
            job.setSalary(faker.number().randomNumber());
            job.setLocation(faker.address().city());
            jobs.add(job);
        }

        jobRepository.saveAll(jobs);
    }
}
