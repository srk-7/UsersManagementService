import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl='http://localhost:7777/api/login'
  private registerUrl='http://localhost:7777/api/register/customer';
  private registerAdminUrl='http://localhost:7777/api/register/admin';
  private getAllUserUrl='http://localhost:7777/api/admin/allCustomers'
  private fetchUserUrl='http://localhost:7777/api/customer/getCustomerByEmail';

  constructor(private http: HttpClient) { }

  login(loginData:any):Observable<any>{
    const headers = { 'Content-Type': 'application/json' };
    return this.http.post(this.apiUrl, loginData,{ headers, responseType: 'text' });
  }

  registerCust(userData: any): Observable<any> {
    const headers = { 'Content-Type': 'application/json' };
    return this.http.post(this.registerUrl, userData,{ headers, responseType: 'text' });
  }

  registerAdmin(userData: any): Observable<any> {
    const headers = { 'Content-Type': 'application/json' };
    return this.http.post(this.registerAdminUrl, userData,{ headers, responseType: 'text' });
  }

  getAllUsers():Observable<any>{
    return this.http.get(this.getAllUserUrl);
  }

  getUserDetails(email: string):Observable<any>{
    const url=`${this.fetchUserUrl}/${email}`;
    return this.http.get(url);
  }

}
