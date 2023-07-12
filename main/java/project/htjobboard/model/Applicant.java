package project.htjobboard.model;

import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applicant")
public class Applicant {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Job[] appliedTo;
    private Job[] wishList;
    private String role;

    public Applicant() {}

    public Applicant(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appliedTo = new Job[1];
        this.wishList = new Job[1];
        this.role = "applicant";

    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Job[] getAppliedTo() {
        return appliedTo;
    }

    public void setAppliedTo(Job[] appliedTo) {
        this.appliedTo = appliedTo;
    }

    public Job[] getWishList() {
        return wishList;
    }

    public void addToWishList(Job job) {
        

        if (wishList == null) {

            wishList = new Job[1];
            wishList[0] = job;

        } else if (wishList.length == 0) {

            wishList = new Job[1];
            wishList[0] = job;

        }
        
        else if (wishList != null && wishList[0] == null) {

            wishList[0] = job;

        } else {
           
            if (!isJobInWishList(job)) {
                Job[] updatedWishList = Arrays.copyOf(wishList, wishList.length + 1);
                updatedWishList[wishList.length] = job;
                wishList = updatedWishList;
            }
        }
    }

    

    private boolean isJobInWishList(Job job) {
        // Check if the wishList is not null
        if (wishList != null && wishList.length > 0 && wishList[0] != null) {
            // Iterate over the wishList array and check if a job with the same ID exists
            for (Job wish : wishList) {
                if (wish.getId().equals(job.getId())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public void removeFromWishList(Job job) {
        // Check if the wishList is null or empty
        if (wishList == null || wishList.length == 0) {
            return; // No jobs in the wishList, nothing to remove
        }

        // Find the index of the job in the wishList
        int index = -1;
        for (int i = 0; i < wishList.length; i++) {
            if (wishList[i].getId().equals(job.getId())) {
                index = i;
                break;
            }
        }

        // If the job is found in the wishList, remove it
        if (index >= 0) {
            Job[] updatedWishList = new Job[wishList.length - 1];
            int destIndex = 0;
            for (int srcIndex = 0; srcIndex < wishList.length; srcIndex++) {
                if (srcIndex != index) {
                    updatedWishList[destIndex++] = wishList[srcIndex];
                }
            }
            wishList = updatedWishList;
        }
    }

    public String getRole() {
        return role;
    }
    
}
