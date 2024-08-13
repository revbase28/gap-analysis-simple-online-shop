import { Injectable } from '@angular/core';
import { baseUrl } from '../../tools/const';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrl: string = baseUrl + 'customer';
  constructor(private http: HttpClient) {}

  fetchCustomer(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  addNewCustomer(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }

  updateCustomer(id: number, formData: FormData): Observable<any> {
    return this.http.patch(this.apiUrl + '/' + id, formData);
  }

  deleteCustoemer(id: number) {
    return this.http.delete<any>(this.apiUrl + '/' + id);
  }
}
