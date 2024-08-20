import { Injectable } from '@angular/core';
import { baseUrl } from '../../tools/const';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  private apiUrl = baseUrl + 'item';

  constructor(private http: HttpClient) {}

  fetchAll(page: number = 0): Observable<any[]> {
    const queryParams = {
      page: page,
      size: 5,
    };

    let params = new HttpParams({ fromObject: queryParams });

    return this.http.get<any[]>(this.apiUrl, { params: params });
  }

  fetchAllWithoutPagination(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl + '-all');
  }

  searchItem(keyword: string, page: number = 0): Observable<any[]> {
    const queryParams = {
      page: page,
      size: 5,
      keyword: keyword,
    };

    let params = new HttpParams({ fromObject: queryParams });

    return this.http.get<any[]>(this.apiUrl + '/search', { params: params });
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
