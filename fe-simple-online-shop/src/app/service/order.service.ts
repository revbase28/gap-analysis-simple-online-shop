import { Injectable } from '@angular/core';
import { baseUrl } from '../../tools/const';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = baseUrl + 'order';

  constructor(private http: HttpClient) {}

  fetchAll(page: number = 0): Observable<any[]> {
    const queryParams = {
      page: page,
      size: 5,
    };

    let params = new HttpParams({ fromObject: queryParams });

    return this.http.get<any[]>(this.apiUrl, { params: params });
  }

  searchOrder(keyword: string, page: number = 0): Observable<any[]> {
    const queryParams = {
      page: page,
      size: 5,
      keyword: keyword,
    };

    let params = new HttpParams({ fromObject: queryParams });

    return this.http.get<any[]>(this.apiUrl + '/search', { params: params });
  }

  addNewOrder(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }

  updateOrder(id: number, formData: FormData): Observable<any> {
    return this.http.patch(this.apiUrl + '/' + id, formData);
  }

  deleteOrder(id: number) {
    return this.http.delete<any>(this.apiUrl + '/' + id);
  }

  getReport() {
    return this.http.get<any>(this.apiUrl + '/report', {
      responseType: 'arraybuffer' as 'json',
    });
  }
}
