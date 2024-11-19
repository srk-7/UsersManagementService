import { CommonModule } from '@angular/common';
import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../user.service';
import { AuthService } from '../auth.service';
 
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  @ViewChild('profileDialogTemplate') profileDialogTemplate!: TemplateRef<any>;
 
  userName: string = '';
  isLoggingIn: boolean = false;
  userDetails = {
    fullName: '',
    emailId: '',
    address: '',
    mobileNumber: '',
    gender: '',
    age: '',
  };
 
  promotions = [
    {
      title: 'Winter Essentials',
      description: 'Up to 30% off on winter clothing',
      image: 'https://static.zara.net/assets/public/5eda/5b68/4b0a46a7be00/b6ce6488a224/image-landscape-932e77dd-44b6-4456-8697-1fd07cd6246b-default_0/image-landscape-932e77dd-44b6-4456-8697-1fd07cd6246b-default_0.jpg?ts=1731691623083&w=1920'
    },
    {
      title: 'Exclusive Deals',
      description: 'Check out the latest offers on top brands',
      image: 'https://static.zara.net/assets/public/57f0/260c/f96742e6b4b5/edf6a031436c/image-landscape-default-3cdb696d-fc8b-4b82-8dd5-0cb0a27aaf8c-default_0/image-landscape-default-3cdb696d-fc8b-4b82-8dd5-0cb0a27aaf8c-default_0.jpg?ts=1731412403132&w=1920'
    },
    {
      title: 'Fall Collection',
      description: 'Discover our latest fall trends',
      image: 'https://static.zara.net/assets/public/3e69/43ec/2a3542119a33/ce221fbf5860/image-landscape-default-90cb2e71-a282-4f0b-b536-271d97e89f8f-default_0/image-landscape-default-90cb2e71-a282-4f0b-b536-271d97e89f8f-default_0.jpg?ts=1731412087949&w=1920'
    },
    {
      title: 'Elegant Outerwear',
      description: 'Stay stylish and warm this winter',
      image: 'https://static.zara.net/assets/public/7156/5199/3a88457d8eaf/07a9c954c58c/image-landscape-default-6a58a0eb-6c15-479e-aa58-f8edb06a856c-default_0/image-landscape-default-6a58a0eb-6c15-479e-aa58-f8edb06a856c-default_0.jpg?ts=1731600443272&w=1920'
    },
    {
      title: 'Casual Looks',
      description: 'Comfort meets style in our casual range',
      image: 'https://static.zara.net/assets/public/7770/7ba9/d4fb4af28f62/adc366b29cda/image-landscape-1149c047-700e-4e38-84d1-96feb6924ac6-default_0/image-landscape-1149c047-700e-4e38-84d1-96feb6924ac6-default_0.jpg?ts=1728907215376&w=1920'
    },
    {
      title: 'Formal Collection',
      description: 'Upgrade your work wardrobe',
      image: 'https://static.zara.net/assets/public/715b/758d/f77c4f46a825/349124f7bd83/image-landscape-8d7ac7a0-36a1-4b69-b572-43b7398bf121-default_0/image-landscape-8d7ac7a0-36a1-4b69-b572-43b7398bf121-default_0.jpg?ts=1731584970935&w=1920'
    }
  ];
 
 
  constructor(
    private dialog: MatDialog,
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) {}
 
  ngOnInit(): void {
    const user = this.userService.getUserDetails();
    if (user) {
      this.userDetails = user;
      this.userName = user.fullName;
      console.log(user);
    } else {
      console.log('User is not logged in');
    }
  }
 
  isProfileDialogOpen: boolean = false;
 
  openProfileDialog(): void {
    this.isProfileDialogOpen = true;
  }
 
  closeProfileDialog(): void {
    this.isProfileDialogOpen = false;
  }
 
  logout(): void {
    console.log('Logging out...');
    this.userService.clearUserDetails();
    this.router.navigate(['/login']);
    window.history.replaceState({}, '', '/login');
  }
}