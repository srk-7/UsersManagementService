import { CommonModule } from '@angular/common';
import { Component, NgModule } from '@angular/core';
import { FormsModule, NgForm, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from "../navbar/navbar.component";
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, NavbarComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username:string='';
  password:string='';

  isLoggingIn: boolean = false; 

  constructor(private router: Router,private authService:AuthService,private userService:UserService) {}

  onSubmit(form: NgForm) {
    if (form.valid) {
      console.log("Username:", form.value.username);
      console.log("Password:", form.value.password);
      this.isLoggingIn = true;
      
        const loginData={
          username:form.value.username,
          password:form.value.password
        };
    
      this.authService.login(loginData).subscribe(
        (response: any) => {
          console.log(response);
          if (response=="Login successful! Redirecting to: Admin Dashboard"){
            this.router.navigate(['/admin'])
          }
          else {
            
            this.authService.getUserDetails(loginData.username).subscribe(
              (userDetails) => {
                console.log(form.value.username);
                console.log(userDetails);
                // Step 3: Store user details (like name, email, etc.)
                this.userService.setUserDetails(userDetails);
                // Redirect to the dashboard
                setTimeout(() => {
                  this.isLoggingIn = false;  // Stop loading spinner
                  this.router.navigate(['/dashboard']);  // Navigate to the dashboard after delay
                }, 2000); 
              },
              (error) => {
                // Handle error while fetching user details
                console.error('Error fetching user details', error);
                // this.errorMessage = 'Failed to fetch user details. Please try again later.';
              }
            );

            // this.router.navigate(['/dashboard']);
          }
          
        },
        error => {
          if (error.status === 401) {
            window.alert('Invalid credentials. Please try again.');
          }}
      );


      // this.router.navigate(['/dashboard']);
      // this.router.navigate(['/admin']);
      form.reset();
    } else {
      console.log("Form is invalid.");
    }
  }

}
