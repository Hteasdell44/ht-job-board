package project.htjobboard.repository;

import project.htjobboard.model.Job;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {

    List<Job> findByTitleContainingAndLocationContaining(String title, String location);

    List<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(String title, String location);

    List<Job> findByTitleContainingIgnoreCase(String title);

    List<Job> findByLocationContainingIgnoreCase(String location);

    List<Job> findByTitleContainingIgnoreCaseAndLocation(String title, String string);
}
