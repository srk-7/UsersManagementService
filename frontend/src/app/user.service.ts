import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() { }



  // Save user details to localStorage
  setUserDetails(user: any): void {
    localStorage.setItem('userDetails', JSON.stringify(user));  // Store the user data as a string
  }

  // Get user details from localStorage
  getUserDetails(): any {
    const user = localStorage.getItem('userDetails');
    return user ? JSON.parse(user) : null;  // Return parsed object or null if not found
  }

  // Clear user details from localStorage (for logout)
  clearUserDetails(): void {
    localStorage.removeItem('userDetails');
  }


}
