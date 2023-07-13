import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

interface Job {
  id: string;
  title: string;
  salary: number;
  location: string;
  role: string;
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent {
  jobs: Job[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;
  jobTitle: string = '';
  location: string = '';
  secondTitle: string = 'Or, Check These Out!';
  myForm: FormGroup;

  constructor(private http: HttpClient, private decimalPipe: DecimalPipe, private formBuilder: FormBuilder) {

    this.http.get<any[]>('https://ht-jobs-4a553258d208.herokuapp.com/jobs').subscribe(response => {
      this.jobs = response;
    });

    this.myForm = this.formBuilder.group({
      jobTitle: ['', Validators.required],
      location: ['', Validators.required],
    });

  }

  onSubmit(form: any): void {
    
    if (form.invalid) {
        return; // Don't proceed if the form is invalid
    }
    
    let jobTitle = form.form.value.jobTitle;
    let location = form.form.value.location;

    let params = new HttpParams();

    if (jobTitle != '') { params = params.append('jobTitle', jobTitle); }
    if (location != '') { params = params.append('location', location); }
    
    
    this.http.get<any[]>('http://localhost:3001/jobs/search', { params }).subscribe((data) => {
      // Process the response data
      this.jobs = data;
      this.secondTitle = "Search Results";
      this.itemsPerPage = 15;
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
