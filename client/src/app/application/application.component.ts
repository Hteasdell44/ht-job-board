import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { AuthService } from '../auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import emailjs from 'emailjs-com';

interface Job {
  id: string;
  title: string;
  salary: number;
  location: string; // lowercase 'string' instead of 'String'
  role: string;
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

@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.css']
})
export class ApplicationComponent {

  currentJob: Job | undefined;
  currentApplicant: Applicant | undefined;
  email: any;
  phone: any;
  address: any;
  city: any;
  state: any;
  resume: any;
  coverLetter: any;
  appSubmitted!: string;
  myForm: FormGroup;

  constructor(private http: HttpClient, private route: ActivatedRoute, private decimalPipe: DecimalPipe, private authService: AuthService, private formBuilder: FormBuilder) {
    const id = this.route.snapshot.paramMap.get('id');
    this.http.get<Job>(`/jobs/${id}`).subscribe(response => {
      this.currentJob = response;
    });

    this.myForm = this.formBuilder.group({
      email: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      resume: ['', Validators.required],
      coverLetter: ['', Validators.required],
    });

  }


  sendEmail(form: any) {

    this.authService.getCurrentUser().subscribe(applicant => {

      this.http.get<any>(`/company/${this.route.snapshot.paramMap.get('id')}`).subscribe(company => {

          const formData = new FormData();
          formData.append('emailAddress', form.form.value.email);
          formData.append('phoneNumber', form.form.value.phone);
          formData.append('homeAddress', form.form.value.address);
          formData.append('city', form.form.value.city);
          formData.append('state', form.form.value.state);
          formData.append('resume', form.form.value.resume);
          formData.append('coverLetter', form.form.value.coverLetter);

          const templateParams = {
            from_name: `${applicant.firstName} ${applicant.lastName}`,
            from_email: `${applicant.email}`,
            to_email: `${company.email}`,
            to_name: `${company.name}`,
            message: 'formData',
          };
        
          // Send the email using the emailjs.send() method
          emailjs.send('service_zp6nai4', 'template_qamzvra', templateParams, 'IVAq6fJqZJenhQh0F')
            .then((response) => {
              console.log('Email sent successfully:', response);
            })
            .catch((error) => {
              console.error('Error sending email:', error);
            });

            this.appSubmitted = `Your Application Has Been Submitted To ${company.name} Successfully ☑️` 

        });

    });

    
    
  }

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const fileList = inputElement.files;

    if (fileList && fileList.length > 0) {
      const selectedFile = fileList[0];

      // Determine the file input based on the element's id or name
      const fileInputName = inputElement.id || inputElement.name;

      // Store the selected file based on the input name
      if (fileInputName === 'resume') {
        this.resume = selectedFile;
      } else if (fileInputName === 'coverLetter') {
        this.coverLetter = selectedFile;
      }
    }
  }

}
