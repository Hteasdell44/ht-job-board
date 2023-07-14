import { Component } from '@angular/core';
import { HomeComponent } from './home/home.component';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  logoPath = '../assets/logo.png';

  constructor(private authService: AuthService) {}

  handleAccountClick(): void {

    if(this.authService.isAuthenticated()) { 
      
      this.authService.getCurrentUser().subscribe(user => {
        if (user) {
          window.location.assign('/profile')
        }
      });

      this.authService.getCurrentCompany().subscribe(user => {
        if (user) {
          window.location.assign('/company/profile/view')
        }
      });
    
    }

    else { window.location.assign('/login') }

  }

  handleJobPostClick() {

    if(this.authService.isAuthenticated()) { 
      
      this.authService.getCurrentUser().subscribe(user => {
        if (user) {
          window.location.assign('/company/login/view')
        }
      });

      this.authService.getCurrentCompany().subscribe(user => {
        if (user) {
          window.location.assign('/company/profile/view')
        }
      });
    
    }

    else { window.location.assign('/company/login/view') }
  }
  
}
