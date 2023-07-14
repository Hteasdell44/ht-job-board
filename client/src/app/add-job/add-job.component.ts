import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { AuthService } from '../auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

interface Job {
  id: string;
  title: string;
  salary: number;
  location: string; // lowercase 'string' instead of 'String'
  role: string;
}

@Component({
  selector: 'app-add-job',
  templateUrl: './add-job.component.html',
  styleUrls: ['./add-job.component.css']
})
export class AddJobComponent {

  jobTitle: any;
  salary!: number;
  city: any;
  jobDescription: any;
  appSubmitted: any;
  myForm: any;

  constructor(private http: HttpClient, private route: ActivatedRoute, private decimalPipe: DecimalPipe, private authService: AuthService, private formBuilder: FormBuilder) {
  
    this.myForm = this.formBuilder.group({
      jobTitle: ['', Validators.required],
      salary: ['', Validators.required],
      city: ['', Validators.required],
      jobDescription: ['', Validators.required],
    });

  }

  handleFormSubmit(form: any) {

    if (form.invalid) {
      console.log('');
        return; // Don't proceed if the form is invalid
    }

    this.authService.getCurrentCompany().subscribe(company => {

      const companyId = company.id;
      const jobTitle = form.form.value.jobTitle;
      const salary = form.form.value.salary;
      const city = form.form.value.city;
      const jobDescription = form.form.value.jobDescription;

    const requestBody = { companyId, jobTitle, salary, city, jobDescription };

      this.http.post<any>(`/company/addJob`, requestBody).subscribe(response => {

      console.log(company);

      if (company.jobListings == null) {
        company.jobListings = []; // Initialize the jobPostings array
      }

        company.jobListings.push(response.job);
        this.appSubmitted = 'Your Job Was Successfully Posted ☑️';
        window.location.assign("/company/profile/view");
      })

    });
  }

}
