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
  { path: '', component: HomeComponent },
  { path: 'signup', component: ApplicantSignupComponent },
  { path: 'login', component: ApplicantLoginComponent },
  { path: 'profile', component: ProfileComponent },    
  { path: 'jobs/:id/view', component: JobDetailsComponent },
  { path: 'jobs/:id/apply', component: ApplicationComponent },
  { path: 'companies', component: CompaniesComponent },
  { path: 'company/login', component: CompanyLoginComponent },
  { path: 'company/signup', component: CompanySignupComponent },
  { path: 'company/profile', component: CompanyProfileComponent },
  { path: 'company/addJob', component: AddJobComponent },
  { path: 'company/:id', component: CompanyDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
