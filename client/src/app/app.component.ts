import { Component } from '@angular/core';
import { HomeComponent } from './home/home.component';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  logoPath = '../assets/logo.png';

  constructor(private authService: AuthService, private router: Router) {}

  handleAccountClick(): void {

    if(this.authService.isAuthenticated()) { 
      
      this.authService.getCurrentUser().subscribe(user => {
        if (user) {
          this.router.navigate(['/profile']);
        }
      });

      this.authService.getCurrentCompany().subscribe(user => {
        if (user) {
          this.router.navigate(['/company/profile/view']);
        }
      });
    
    }

    else { this.router.navigate(['/login']) }

  }

  handleJobPostClick() {

    if(this.authService.isAuthenticated()) { 
      
      this.authService.getCurrentUser().subscribe(user => {
        if (user) {
          this.router.navigate(['/company/login/view']);
        }
      });

      this.authService.getCurrentCompany().subscribe(user => {
        if (user) {
          this.router.navigate(['/company/profile/view']);
        }
      });
    
    }

    else { this.router.navigate(['/company/login/view']) }
  }
  
}
