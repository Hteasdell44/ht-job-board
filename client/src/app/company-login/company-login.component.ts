import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-company-login',
  templateUrl: './company-login.component.html',
  styleUrls: ['./company-login.component.css']
})
export class CompanyLoginComponent {

  bool: Boolean = false;
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private http: HttpClient) {
    
    if(this.authService.isAuthenticated()) {

      this.authService.getCurrentUser().subscribe(user => {

        console.log(user.role);

        if (user.role != 'company') {

          this.bool = true;

        } else if (user.role == 'company') {

          window.location.assign('/company/profile')
        }

      });

    }
  }

  onSubmit(form: any): void {

    const email = form.form.value.email;
    const password = form.form.value.password;

    const requestBody = { email, password };

    this.http.post<any>('http://localhost:3001/company/login', requestBody).subscribe(response => {

      console.log(response);
      localStorage.setItem('token', response.token);
      window.location.assign('/company/profile');
    
    });
    
  }

  logout(): void {

    this.authService.logout();
    window.location.reload();

  }

}
