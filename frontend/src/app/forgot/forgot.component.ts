import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { NavbarComponent } from "../navbar/navbar.component";

@Component({
  selector: 'app-forgot',
  standalone: true,
  imports: [FormsModule, CommonModule, NavbarComponent],
  templateUrl: './forgot.component.html',
  styleUrl: './forgot.component.css'
})
export class ForgotComponent {

   
   onSubmit(form: NgForm) {
    if (form.valid) {
      console.log("Password reset link sent to:", form.value.email);
      form.reset();
      
    } else {
      console.log("Form is invalid.");
    }
  }

}
