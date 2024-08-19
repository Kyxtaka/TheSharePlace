import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenUtilService } from '../token/token-util.service';

export interface UserData {
  userId: number,
  username: string,
  firstname: string,
  lastname: string,
  email: string,
  roles: Privilege[] | any | null,
  groups: Group[] 
}

export interface CredAssociation {
  groupId: number,
  credId: number
};

export interface Credential {
  id: number,
  email: string,
  identifier: string,
  password: string,
  a2f: boolean,
  platformId: number,
  groupId: number
};

export interface Platform {
  id: number,
  name: string,
  url: string,
  imgRef: string
}

export interface Group {
  id: number,
  UID: number,
  name: string,
  description: string,
  password: string,
};

export enum Privilege {
  USER = "USER",
  ADMIN = "ADMIN"
};

@Injectable({
  providedIn: 'root'
})
export class APIServiceAccount {

  private apiUrl = "http://localhost:8080/api/data/account"

  constructor(private http: HttpClient, private tokenService: TokenUtilService) { }

  findBy(filter: string, value: string | number): Observable<any> {
    const params = new HttpParams().set("search_query", value);
    return this.http.get(`${this.apiUrl}/search/${filter}`, {params, headers: this.tokenService.getHttpRequestHeader()})
  }

  add(username: string, email: string, A2F:boolean, password: string): Observable<any> {
    const headers = this.tokenService.getHttpRequestHeader();
    const body =  {"username":username, "email":email, "A2F":A2F, "password":password}
    console.log("request body:", body)
    return this.http.post(`${this.apiUrl}/register`, body, {headers})
  }

}

@Injectable({
  providedIn: 'root' // This makes the service available application-wide
})
export class APIServiceGroup {
  private apiUrl = "http://localhost:8080/api/data/group";

  constructor(
    private http: HttpClient, 
    private tokenService: TokenUtilService) {}

    findBy(filter:string,id:number): Observable<any> {
      return this.http.get(`${this.apiUrl}/find/${filter}/${id}`,{headers: this.tokenService.getHttpRequestHeader() });
    }

    add(groupName:string, groupDescription:string, groupPassword: string) {
      const body = {groupName, groupDescription, groupPassword} 
      return this.http.post(`${this.apiUrl}/create`,body,{headers: this.tokenService.getHttpRequestHeader() });
    }

}

@Injectable({
  providedIn: 'root' // This makes the service available application-wide
})
export class APIServiceUser {
   private apiUrl = "http://localhost:8080/api/data/user"
   private authAPIURL  = "http://localhost:8080/api/auth"

   constructor(
    private http: HttpClient, 
    private tokenService: TokenUtilService) {}

    findByUsername(username:string): Observable<any> {
      return this.http.get(`${this.apiUrl}/find/username/${username}`, {headers: this.tokenService.getHttpRequestHeader()});
    }

    register(firstname:string, lastname:string, email:string, username:string, password:string ): Observable<any> {
      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      const body = { firstname, lastname, email, username, password }
      return this.http.post(`${this.authAPIURL}/register`, body, {headers}) 
    }
}

@Injectable({
  providedIn: 'root' // This makes the service available application-wide
})
export class APIServicePlatform {
   private apiUrl = "http://localhost:8080/api/data/paltform"

   constructor(
    private http: HttpClient, 
    private tokenService: TokenUtilService) {}

    findById(idValut: number): Observable<any> {
      return this.http.get(`${this.apiUrl}/platform/find/${idValut}`, { headers: this.tokenService.getHttpRequestHeader() });
    }

}

