import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser: any;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post('/login', { email, password });
  }

  logout(): void {
    localStorage.removeItem('token');
    this.currentUser = null; // Reset the current user when logging out
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  getCurrentUser(): Observable<any> {
    const token = localStorage.getItem('token');
    if (token) {
      // Make an authenticated request to the backend to retrieve the current user's data
      return this.http.get<any>('/applicant/me', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }).pipe(
        tap((user: any) => {
          this.currentUser = user; // Store the current user data in the service
          return this.currentUser;
        })
      );
    } else {
      return of(null);
    }
  }

  getCurrentCompany(): Observable<any> {
    const token = localStorage.getItem('token');
    if (token) {
      // Make an authenticated request to the backend to retrieve the current user's data
      return this.http.get<any>('/company/me', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }).pipe(
        tap((user: any) => {
          this.currentUser = user; // Store the current user data in the service
          return this.currentUser;
        })
      );
    } else {
      return of(null);
    }
  }

  setCurrentUser(user: any): void {
    this.currentUser = user;
  }
}