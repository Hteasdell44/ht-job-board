package project.htjobboard.repository;

import project.htjobboard.model.Applicant;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicantRepository extends MongoRepository<Applicant, String> {
    boolean existsByEmail(String email);

    Optional<Applicant> findByEmail(String email);
}