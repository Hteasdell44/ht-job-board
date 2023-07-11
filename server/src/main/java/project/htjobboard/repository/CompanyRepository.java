package project.htjobboard.repository;

import project.htjobboard.model.Company;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

    boolean existsByEmail(String email);
    Optional<Company> findByEmail(String email);
    List<Company> findByNameContainingIgnoreCase(String companyName);
    
}
