import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NavbarComponent } from "../navbar/navbar.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(private router: Router) {}

  navigateToLogin() {
    this.router.navigate(['/login']);
    console.log("hebfh");
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }

  navigateToAdminRegister() {
    // Route to admin registration
    this.router.navigate(['/admin-register']);
}
}

