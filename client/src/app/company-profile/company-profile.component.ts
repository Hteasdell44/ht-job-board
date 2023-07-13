import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { DecimalPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface Job {
  id: string;
  title: string;
  salary: number;
  location: string;
  role: string;
}

@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.css']
})
export class CompanyProfileComponent {

  postedJobs: Job[] = [];

  constructor(private authService: AuthService, private decimalPipe: DecimalPipe, private http: HttpClient) {

    this.authService.getCurrentCompany().subscribe(company => {
      this.postedJobs = company.jobListings;
    });
  }

  deleteJob(job: Job): void {

    if (this.authService.isAuthenticated()) {

      this.authService.getCurrentCompany().subscribe(company => {

        const currentJobId = job.id;

        const requestBody = {
          applicantEmail: company.email,
          jobId: currentJobId
        };
    
        const headers = {
          Authorization: `Bearer ${localStorage.getItem('token')}` // Add the token to the Authorization header
        };
    
        this.http.post<any>('/company/deleteJob', requestBody, { headers }).subscribe(response => {
          console.log(response);
          window.location.reload();
        });
      });

    }
  }

  formatSalary(salary: number): string {
    const formattedSalary = this.decimalPipe.transform(salary, '1.0-0');
    return formattedSalary || '0';
  }

  logout(): void {
    this.authService.logout();
    window.location.assign('');
  }
}
