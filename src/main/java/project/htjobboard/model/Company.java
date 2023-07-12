package project.htjobboard.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
public class Company {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Job[] jobListings;
    private String role;

    public Company() {}

    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "company";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Job[] getJobListings() {
        return jobListings;
    }

    public void setJobListings(Job[] array) {
        this.jobListings = array;
    }

    public void addToJobListings(Job job) {

        if (jobListings == null) {

            jobListings = new Job[1];
            jobListings[0] = job;
            return;

        }

        if (jobListings == null) {

            jobListings = new Job[1];
            jobListings[0] = job;
            return;

        } else if (jobListings.length == 0) {

            jobListings = new Job[1];
            jobListings[0] = job;

        }
        
        else if (jobListings != null && jobListings[0] == null) {

            jobListings[0] = job;

        } else {
           
            Job[] updatedJobListing = Arrays.copyOf(jobListings, jobListings.length + 1);
            updatedJobListing[jobListings.length] = job;
            jobListings = updatedJobListing;
        }
        
    }



    public void removeFromJobListings(Job job) {

        // Check if the wishList is null or empty
        if (jobListings == null || jobListings.length == 0) {
            return; // No jobs in the wishList, nothing to remove
        }

        // Find the index of the job in the wishList
        int index = -1;
        for (int i = 0; i < jobListings.length; i++) {
            if (jobListings[i].getId().equals(job.getId())) {
                index = i;
                break;
            }
        }

        // If the job is found in the wishList, remove it
        if (index >= 0) {
            Job[] updatedWishList = new Job[jobListings.length - 1];
            int destIndex = 0;
            for (int srcIndex = 0; srcIndex < jobListings.length; srcIndex++) {
                if (srcIndex != index) {
                    updatedWishList[destIndex++] = jobListings[srcIndex];
                }
            }
            jobListings = updatedWishList;
        }
    }

}
