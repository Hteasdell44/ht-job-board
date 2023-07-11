import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicantSignupComponent } from './applicant-signup.component';

describe('ApplicantSignupComponent', () => {
  let component: ApplicantSignupComponent;
  let fixture: ComponentFixture<ApplicantSignupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicantSignupComponent]
    });
    fixture = TestBed.createComponent(ApplicantSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
