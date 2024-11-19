import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

export interface User {
  fullName: string;
  emailId: string;
  address:string;
  age:number;
  gender:string;
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  constructor(private authService:AuthService,private router: Router) {}

  
  products = [
    { id: 1, name: 'Product 1', price: 100 },
    { id: 2, name: 'Product 2', price: 200 }
  ];
  users: User[] = [];

  showModal = false;
  currentAction = ''; 
  currentProduct : { id: number, name: '', price: number }={id:0,name:'',price:0};
  currentView = ''; 

  
  showActionModal(action: string, product?: any) {
    this.currentAction = action;
    if (action === 'add') {
      this.currentProduct = { id: 0, name: '', price: 0 }; 
    } else if (action === 'update' && product) {
      this.currentProduct = { ...product }; 
    } else if (action === 'delete' && product) {
      this.currentProduct = { ...product }; 
    }
    this.showModal = true;
  }

  
  addProduct() {
    this.products.push({ ...this.currentProduct, id: this.products.length + 1 });
    this.closeModal();
  }

  
  updateProduct() {
    const index = this.products.findIndex(p => p.id === this.currentProduct.id);
    if (index !== -1) {
      this.products[index] = { ...this.currentProduct };
    }
    this.closeModal();
  }

  
  deleteProduct() {
    this.products = this.products.filter(p => p.id !== this.currentProduct.id);
    this.closeModal();
  }

  
  closeModal() {
    this.showModal = false;
    this.currentAction = '';
    this.currentProduct = { id: 0, name: '', price: 0 };
  }

  
  toggleView(view: string) {
    this.currentView = view;
    if(view=="users"){
      this.fetchUsers();
    }
  }

  fetchUsers(): void {
    this.authService.getAllUsers().subscribe(
      (users) => {
        console.log(users);
        this.users = users;  
         
      },
      (error) => {
        console.error('Error fetching users:', error);
         
      }
    );
  }

  logout(): void {
    this.router.navigate(['/login']);
    
    window.location.href = '/login';  
  }

}
