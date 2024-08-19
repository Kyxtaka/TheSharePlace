import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import * as jwt_decode from 'jwt-decode'; // Correct import for jwt-decode

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth/login'; // URL de votre endpoint d'authentification

  constructor(private http: HttpClient,private cookieService: CookieService) { }

  login(identifier: string, password: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { identifier, password };
    return this.http.post(this.apiUrl, body, { headers });
  }

  logout(): void {
    this.cookieService.delete('authToken');
  }

}


