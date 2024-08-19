import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import * as jwt_decode from 'jwt-decode'; // Correct import for jwt-decode
import { catchError, Observable, of } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TokenUtilService {

  constructor(private cookieService: CookieService) { }

  setToken(token: string): void {
    const decodedToken: any = jwt_decode.jwtDecode(token);
    const exp = decodedToken.exp * 1000; // Convert to milliseconds
    this.cookieService.set('authToken', token, { expires: new Date(exp), secure: true });
  }

  getJwtTokenFromCookie(): string | undefined {
    return this.cookieService.get('authToken');
  }

  isTokenExpired(token: string): boolean {
    try {
      const decoded: any = jwt_decode.jwtDecode(token);
      const expirationDate = new Date(decoded.exp * 1000); // exp is in seconds
      return expirationDate < new Date();
    } catch (error) {
      return true;
    }
  }

  extractTokenUsername(token:string): string {
    try {
      const decoded: any = jwt_decode.jwtDecode(token);
      const username = decoded.sub; // exp is in seconds
      return username;
    } catch (error) {
      return "None";
    }
  }

  //CSRF is not working, so its disabled
  getCsrfTokenFromCookiesInit(): string | null {
    const csrfToken = document.cookie.split('; ')
    .find(row => row.startsWith('XSRF-TOKEN='))
    ?.split('=')[1];
    return null;
  }

  // Return Header with the specific tokens saved in cookies
  getHttpRequestHeader(): HttpHeaders {
    const header = new HttpHeaders ({
        'Content-Type': 'application/json',
        'X-XSRF-TOKEN': `${null}`, // Add CSRF token to headers
        'Authorization': `Bearer ${this.getJwtTokenFromCookie()}`, // Add JWT token to headers
      })
    return header 
  }
}
