import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-company-signup',
  templateUrl: './company-signup.component.html',
  styleUrls: ['./company-signup.component.css']
})
export class CompanySignupComponent {

  email: string = '';
  companyName: string = '';
  password: string = '';
  passwordConfirm: string = '';

  constructor(private http: HttpClient, private authService: AuthService) { }

  onSubmit(form: any): void {

    const companyName = form.form.value.companyName;
    const email = form.form.value.email;
    const password = form.form.value.password;
    const passwordConfirm = form.form.value.passwordConfirm;

    const requestBody = { companyName, email, password, passwordConfirm };

    this.http.post<any>('/company/signup', requestBody).subscribe(response => {

      console.log(response);
      localStorage.setItem('token', response.token);
      this.authService.setCurrentUser(response.user);
      window.location.assign('/company/profile/view');
    
    });
    
  }

}
