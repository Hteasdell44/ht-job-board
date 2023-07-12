package project.htjobboard.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.htjobboard.model.Job;
import project.htjobboard.repository.JobRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/jobs")
public class JobController {
    
    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping()
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Job> getSpecificJob(@PathVariable("id") String id) {
        return jobRepository.findById(id);
    }

   @GetMapping("/search")
    public List<Job> searchByTitleAndLocation(@RequestParam(value = "title", required = false) String title, @RequestParam(value = "location", required = false) String location) {

        if (title == null && location == null) {
            return jobRepository.findAll();
        } else if (title != null && location != null) {
            return jobRepository.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(title, location);
        } else if (title != null && location == null) {
            // Modify the condition to include jobs with any location
            return jobRepository.findByTitleContainingIgnoreCaseAndLocation(title, "");
        } else if (title == null && location != null) {
            return jobRepository.findByLocationContainingIgnoreCase(location);
        } else {
            return new ArrayList<>(); // Return an empty array as a fallback
        }
    }

}
