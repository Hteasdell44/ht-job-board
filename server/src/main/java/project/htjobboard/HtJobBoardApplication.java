package project.htjobboard;

import project.htjobboard.service.JobService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HtJobBoardApplication {

    private final JobService jobService;

    @Autowired
    public HtJobBoardApplication(JobService jobService) {
        this.jobService = jobService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HtJobBoardApplication.class, args);
    }
	
    @PostConstruct
    public void init() {
        jobService.seedJobs();
    }
}
