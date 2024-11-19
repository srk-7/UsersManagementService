import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NavbarComponent } from "../navbar/navbar.component";
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-admin-registration',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, NavbarComponent],
  templateUrl: './admin-registration.component.html',
  styleUrls: ['./admin-registration.component.css']
})
export class AdminRegistrationComponent {
  
  adminRegisterForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router,private authService:AuthService) {
    this.adminRegisterForm = this.fb.group({
      fullName: ['', Validators.required],
      address: ['', Validators.required],
      emailId: ['', [Validators.required, Validators.email]],
      mobileNumber: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      age: ['', [Validators.required, Validators.min(10)]],
      gender: ['', Validators.required],
      password: [
        '', 
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?#&]{8,}$/)
        ]
      ]
    });
  }

  onSubmit() {
    if (this.adminRegisterForm.valid) {
      console.log('Admin registration successful', this.adminRegisterForm.value);

      this.authService.registerAdmin(this.adminRegisterForm.value).subscribe(
        response => {
          console.log('Registration successful:', response);
          alert('Registration successful!');
          // this.router.navigate(['/login']);
        },
        error => {
          console.error('Registration failed:', error);
          alert('Registration failed. Please try again.');
        }
      );

      this.router.navigate(['/home']);
      
    } else {
      console.log('Admin registration form is invalid');
    }
  }
}
