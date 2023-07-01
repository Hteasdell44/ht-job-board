import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent {

  constructor(private http: HttpClient) {}

  getAllJobs() {
    
    this.http.get('http://localhost:3001/jobs').subscribe(response => {

    return response;
    
    });

  }

}

