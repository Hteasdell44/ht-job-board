import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-applicant-signup',
  templateUrl: './applicant-signup.component.html',
  styleUrls: ['./applicant-signup.component.css']
})
export class ApplicantSignupComponent {

  email: string = '';
  firstName: string = '';
  lastName: string = '';
  password: string = '';
  passwordConfirm: string = '';


  constructor(private http: HttpClient, private authService: AuthService) { }

  onSubmit(form: any): void {

    const firstName = form.form.value.firstName;
    const lastName = form.form.value.lastName;
    const email = form.form.value.email;
    const password = form.form.value.password;
    const passwordConfirm = form.form.value.passwordConfirm;

    const requestBody = { firstName, lastName, email, password, passwordConfirm };

    this.http.post<any>('http://localhost:3001/applicant/signup', requestBody).subscribe(response => {

      console.log(response);
      localStorage.setItem('token', response.token);
      this.authService.setCurrentUser(response.user);
      window.location.assign('/profile');
    
    });
    
  }
}
