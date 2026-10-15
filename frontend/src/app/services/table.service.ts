import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { RestaurantTable, TableRequest, TableStatus } from '../models/models';

@Injectable({ providedIn: 'root' })
export class TableService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/tables`;

  getAll(): Observable<RestaurantTable[]> {
    return this.http.get<RestaurantTable[]>(this.url);
  }

  create(request: TableRequest): Observable<RestaurantTable> {
    return this.http.post<RestaurantTable>(this.url, request);
  }

  update(id: number, request: TableRequest): Observable<RestaurantTable> {
    return this.http.put<RestaurantTable>(`${this.url}/${id}`, request);
  }

  updateStatus(id: number, status: TableStatus): Observable<RestaurantTable> {
    return this.http.patch<RestaurantTable>(`${this.url}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
