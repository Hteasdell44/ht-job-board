import { DecimalPipe } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';

interface Company {
  id: string;
  name: string;
  email: string;
  password: string;
  role: string;
}

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.css']
})
export class CompaniesComponent {

  companies: Company[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;
  companyName: any;
  secondTitle: string = 'Or, Start Here!'

  constructor(private http: HttpClient, private decimalPipe: DecimalPipe) {

    this.http.get<any[]>('/company/companies').subscribe(response => {
      this.companies = response;
    });

  }

  onSubmit(form: any): void {
    
    let companyName = form.form.value.companyName;

    let params = new HttpParams();
    params = params.append('companyName', companyName);

    this.http.get<any[]>('/company/search', { params }).subscribe((data) => {

      this.companies = data;
      this.secondTitle = "Search Results";
      this.itemsPerPage = 10;
    });
  }

  getPageItems(): Company[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return this.companies.slice(startIndex, endIndex);
  }

  nextPage() {
    this.currentPage++;
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

}
