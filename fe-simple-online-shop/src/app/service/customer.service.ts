import { Injectable } from '@angular/core';
import { baseUrl } from '../../tools/const';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrl: string = baseUrl + 'customer';
  constructor(private http: HttpClient) {}

  fetchCustomer(page: number = 0): Observable<any[]> {
    const queryParams = {
      page: page,
      size: 5,
    };

    let params = new HttpParams({ fromObject: queryParams });

    return this.http.get<any[]>(this.apiUrl, { params: params });
  }

  fetchCustomerWithoutPagination(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl + '-all');
  }

  searchCustomer(keyword: string, page: number = 0): Observable<any[]> {
    const queryParams = {
      page: page,
      size: 5,
      keyword: keyword,
    };

    let params = new HttpParams({ fromObject: queryParams });

    return this.http.get<any[]>(this.apiUrl + '/search', { params: params });
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
