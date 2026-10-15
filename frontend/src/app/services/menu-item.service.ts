import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { MenuItem, MenuItemRequest } from '../models/models';

@Injectable({ providedIn: 'root' })
export class MenuItemService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/menu-items`;

  getAll(categoryId?: number): Observable<MenuItem[]> {
    let params = new HttpParams();
    if (categoryId) {
      params = params.set('categoryId', categoryId);
    }
    return this.http.get<MenuItem[]>(this.url, { params });
  }

  create(request: MenuItemRequest): Observable<MenuItem> {
    return this.http.post<MenuItem>(this.url, request);
  }

  update(id: number, request: MenuItemRequest): Observable<MenuItem> {
    return this.http.put<MenuItem>(`${this.url}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
