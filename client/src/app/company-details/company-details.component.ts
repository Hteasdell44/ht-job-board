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
}

interface Company {

  name: string;
  email: string;
  password: string;
  jobListings: Job[];
  role: string;

}

@Component({
  selector: 'app-company-details',
  templateUrl: './company-details.component.html',
  styleUrls: ['./company-details.component.css']
})
export class CompanyDetailsComponent {

  currentCompany!: Company;
  jobs: Job[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;

  constructor(private http: HttpClient, private route: ActivatedRoute, private decimalPipe: DecimalPipe, private authService: AuthService) {

    const id = this.route.snapshot.paramMap.get('id');

    this.http.get<Company>(`/company/current/${id}`).subscribe(response => {
      console.log(response);
      this.currentCompany = response;
    });

  }

  getPageItems(): Job[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return this.jobs.slice(startIndex, endIndex);
  }

  nextPage() {
    this.currentPage++;
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }
  
  formatSalary(salary: number): string {
    const formattedSalary = this.decimalPipe.transform(salary, '1.0-0');
    return formattedSalary || '0';
  }

}