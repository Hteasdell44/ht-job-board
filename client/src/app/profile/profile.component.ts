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
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  wishList: Job[] = [];

  constructor(private authService: AuthService, private decimalPipe: DecimalPipe, private http: HttpClient) {

    this.authService.getCurrentUser().subscribe(applicant => {
      this.wishList = applicant.wishList;

      console.log(this.wishList);
    });
  }

  logout(): void {
    this.authService.logout();
    window.location.assign('');
  }

  formatSalary(salary: number): string {
    const formattedSalary = this.decimalPipe.transform(salary, '1.0-0');
    return formattedSalary || '0';
  }

  removeFromWishList(job: Job): void {

    if (this.authService.isAuthenticated()) {

      this.authService.getCurrentUser().subscribe(applicant => {

        const currentJobId = job.id;

        const requestBody = {
          applicantEmail: applicant.email,
          jobId: currentJobId
        };
    
        const headers = {
          Authorization: `Bearer ${localStorage.getItem('token')}` // Add the token to the Authorization header
        };
    
        this.http.post<any>('/applicant/removeFromWishList', requestBody, { headers }).subscribe(response => {
          console.log(response);
          window.location.reload();
        });
      });

    }
  }

}
