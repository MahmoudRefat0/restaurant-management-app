import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly storageKey = 'auth';

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${environment.apiUrl}/auth/login`, { username, password })
      .pipe(tap(response => localStorage.setItem(this.storageKey, JSON.stringify(response))));
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    this.router.navigate(['/login']);
  }

  get currentUser(): AuthResponse | null {
    const data = localStorage.getItem(this.storageKey);
    return data ? JSON.parse(data) : null;
  }

  get token(): string | null {
    return this.currentUser?.token ?? null;
  }

  isLoggedIn(): boolean {
    return this.token !== null;
  }

  isAdmin(): boolean {
    return this.currentUser?.role === 'ADMIN';
  }
}
