import { AuthService } from './../auth.service';
import { CommonModule } from '@angular/common';
import { Component} from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NavbarComponent } from "../navbar/navbar.component";
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, NavbarComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router,private authService:AuthService) {
    this.registerForm = this.fb.group({
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
    if (this.registerForm.valid) {
      console.log('Registration successful', this.registerForm.value);

      this.authService.registerCust(this.registerForm.value).subscribe(
        response => {
          console.log('Registration successful:', response);
          alert('Registration successful!');
          // this.router.navigate(['/login']);
        },
        error => {
          if(error.status===409){
            console.error(error);
            alert("Email ID Already Registered.Try New One!!!!");
          }else{
          console.error('Registration failed:', error);
          alert('Registration failed. Please try again.');}
        }
      );


      this.router.navigate(['/home']);
    } else {
      console.log('Registration form is invalid');
    }
  }
}