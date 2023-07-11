import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { JobDetailsComponent } from './job-details/job-details.component';
import { DecimalPipe } from '@angular/common';
import { ApplicationComponent } from './application/application.component';
import { ApplicantLoginComponent } from './applicant-login/applicant-login.component';
import { ApplicantSignupComponent } from './applicant-signup/applicant-signup.component';
import { FormsModule } from '@angular/forms';
import { ProfileComponent } from './profile/profile.component';
import { CompaniesComponent } from './companies/companies.component';
import { CompanyLoginComponent } from './company-login/company-login.component';
import { CompanySignupComponent } from './company-signup/company-signup.component';
import { CompanyProfileComponent } from './company-profile/company-profile.component';
import { AddJobComponent } from './add-job/add-job.component';
import { CompanyDetailsComponent } from './company-details/company-details.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    JobDetailsComponent,
    ApplicationComponent,
    ApplicantLoginComponent,
    ApplicantSignupComponent,
    ProfileComponent,
    CompaniesComponent,
    CompanyLoginComponent,
    CompanySignupComponent,
    CompanyProfileComponent,
    AddJobComponent,
    CompanyDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [DecimalPipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
