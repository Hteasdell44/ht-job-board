package project.htjobboard;

import project.htjobboard.service.JobService;
import project.htjobboard.service.CompanyService;
import project.htjobboard.repository.CompanyRepository;
import project.htjobboard.repository.JobRepository;


import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("project.htjobboard")
public class HtJobBoardApplication {

    private final JobService jobService;
    private final CompanyService companyService;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public HtJobBoardApplication(JobService jobService, CompanyService companyService, JobRepository jobRepository, CompanyRepository companyRepository) {
        this.jobService = jobService;
        this.companyService = companyService;
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(HtJobBoardApplication.class);
    }
	
    @PostConstruct
    public void init() {

        if (!dataAlreadyExists()) {

            companyService.seedCompanies();

        }
    }

    private boolean dataAlreadyExists() {
    long companyCount = companyRepository.count();
    long jobCount = jobRepository.count();

    // Assuming data already exists if either company or job count is greater than zero
    return (companyCount > 0 || jobCount > 0);
}

}
