import { Injectable } from '@angular/core';
import { baseUrl } from '../../tools/const';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = baseUrl + 'order';

  constructor(private http: HttpClient) {}

  fetchAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
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
