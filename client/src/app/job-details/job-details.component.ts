import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { AuthService } from '../auth.service';

interface Job {
  id: string;
  title: string;
  salary: number;
  location: string; // lowercase 'string' instead of 'String'
  role: string;
  description: string;
  company: { $ref: string; $id: string; name: string };
}

interface Applicant {
  id: string ;
  firstName: string ;
  lastName: string ;
  email: string ;
  password: string ;
  appliedTo: Job[];
  wishList: Job[];
}


interface Company {

  name: string;
  email: string;
  password: string;
  jobListings: Job[];
  role: string;

}

@Component({
  selector: 'app-job-details',
  templateUrl: './job-details.component.html',
  styleUrls: ['./job-details.component.css']
})

export class JobDetailsComponent {

  currentJob: Job | undefined;
  currentApplicant: Applicant | undefined;
  addedToWishList!: string;
  currentCompany: Company | undefined;

  constructor(private http: HttpClient, private route: ActivatedRoute, private decimalPipe: DecimalPipe, private authService: AuthService) {
    const id = this.route.snapshot.paramMap.get('id');
    this.http.get<Job>(`/jobs/${id}`).subscribe(response => {
      this.currentJob = response;
      console.log(response)
    });

    this.http.get<Company>(`/company/${id}`).subscribe(response => {
      this.currentCompany = response;
    });
  }

  formatSalary(salary: number): string {
    const formattedSalary = this.decimalPipe.transform(salary, '1.0-0');
    return formattedSalary || '0';
  }

  handleApplyClick(): void {

    if (this.authService.isAuthenticated()) {
      
      this.authService.getCurrentUser().subscribe(applicant => {

        if (applicant) {

          window.location.assign(`/jobs/${this.currentJob?.id}/apply`)

        }
      });

      this.authService.getCurrentCompany().subscribe(company => {

        if (company) {

          this.addedToWishList = 'You must sign out of your Company account to apply to a Job!'
          return;

        } else {

          window.location.assign(`/jobs/${this.currentJob?.id}/apply`)

        }
      });

    
    } else { 
      
      window.location.assign('/login')
    
    }

  }

  handleWishListClick(): void {

    if (this.authService.isAuthenticated()) {

      this.authService.getCurrentCompany().subscribe(company => {

        if (company) {

          this.addedToWishList = 'You must sign out of your Company account to add jobs to your wishlist!'
          return;
        }
      });

      this.authService.getCurrentUser().subscribe(applicant => {

        console.log(applicant.email);

        const currentJobId = this.currentJob?.id;

        const requestBody = {
          applicantEmail: applicant.email,
          jobId: currentJobId
        };
    
        const headers = {
          Authorization: `Bearer ${localStorage.getItem('token')}` // Add the token to the Authorization header
        };
    
        this.http.post<any>('/applicant/addToWishList', requestBody, { headers }).subscribe(response => {
          console.log(response);
          this.addedToWishList = "Added To Your List ❤️";
        });
      });
  
      

    } else {

      window.location.assign('/login');

    }
  }

}
