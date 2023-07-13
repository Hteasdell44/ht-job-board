import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { JobDetailsComponent } from './job-details/job-details.component';
import { ApplicationComponent } from './application/application.component';
import { ApplicantSignupComponent } from './applicant-signup/applicant-signup.component';
import { ApplicantLoginComponent } from './applicant-login/applicant-login.component';
import { ProfileComponent } from './profile/profile.component';
import { CompaniesComponent } from './companies/companies.component';
import { CompanyLoginComponent } from './company-login/company-login.component';
import { CompanySignupComponent } from './company-signup/company-signup.component';
import { CompanyProfileComponent } from './company-profile/company-profile.component';
import { AddJobComponent } from './add-job/add-job.component';
import { CompanyDetailsComponent } from './company-details/company-details.component';

const routes: Routes = [
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/', component: HomeComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/signup', component: ApplicantSignupComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/login', component: ApplicantLoginComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/profile', component: ProfileComponent },    
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/jobs/:id', component: JobDetailsComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/jobs/:id/apply', component: ApplicationComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/companies', component: CompaniesComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/company/login', component: CompanyLoginComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/company/signup', component: CompanySignupComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/company/profile', component: CompanyProfileComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/company/addJob', component: AddJobComponent },
  { path: 'https://ht-jobs-4a553258d208.herokuapp.com/company/:id', component: CompanyDetailsComponent },
  
  
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
