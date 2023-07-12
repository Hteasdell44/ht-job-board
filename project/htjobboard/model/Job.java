package project.htjobboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job")
public class Job {
    @Id
    private String id;
    private String title;
    private double salary;
    private String location;
    private String description;

    @DBRef
    private Company company;

    public Job() {
    }

    public Job(String title, double salary, String location) {
        this.title = title;
        this.salary = salary;
        this.location = location;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary2) {
        this.salary = salary2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String jobDescription) {
        this.description = jobDescription;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}

