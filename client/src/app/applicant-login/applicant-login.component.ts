import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-applicant-login',
  templateUrl: './applicant-login.component.html',
  styleUrls: ['./applicant-login.component.css']
})
export class ApplicantLoginComponent {

  email: string = '';
  password: string = '';


  constructor(private http: HttpClient, private authService: AuthService) { }

  onSubmit(form: any): void {

    const email = form.form.value.email;
    const password = form.form.value.password;

    const requestBody = { email, password };

    this.http.post<any>('http://localhost:3001/applicant/login', requestBody).subscribe(response => {

      console.log(response);
      localStorage.setItem('token', response.token);
      window.location.assign('/profile');
    
    });
    
  }

}
