import { Injectable } from '@angular/core';
import { baseUrl } from '../../tools/const';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  private apiUrl = baseUrl + 'item';

  constructor(private http: HttpClient) {}

  fetchAll(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  addNewItem(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }

  updateItem(id: number, formData: FormData): Observable<any> {
    return this.http.patch(this.apiUrl + '/' + id, formData);
  }

  deleteItem(id: number) {
    return this.http.delete<any>(this.apiUrl + '/' + id);
  }
}
