import { Component, OnInit, ViewChild } from '@angular/core';
import { Customer } from '../../../tools/typedef';
import { CustomerService } from '../../service/customer.service';
import moment from 'moment';
import { NgIf } from '@angular/common';
import { DialogConfimationComponent } from '../dialog-confimation/dialog-confimation.component';
import { FormsModule, NgForm } from '@angular/forms';
import { read } from 'fs';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [NgIf, DialogConfimationComponent, FormsModule],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css',
})
export class CustomerComponent implements OnInit {
  customers: Customer[] = [];
  isConfirmDialogOpen: boolean = false;
  isAddCusFormOpen: boolean = false;
  isEditCusFormOpen: boolean = false;
  selectedAvatarFile: File | undefined;
  imagePreview: string | ArrayBuffer | null = null;

  emptyCustomer: Customer = {
    customerName: '',
    customerAddress: '',
    customerCode: '',
    customerId: -1,
    customerPhone: '',
    lastOrderDate: new Date(),
    isActive: false,
    pic: '',
  };

  selectedCustomer: Customer = this.emptyCustomer;

  @ViewChild('addForm') addForm: NgForm | undefined;
  @ViewChild('editForm') editForm: NgForm | undefined;

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.fetchCustomer();
  }

  onFileChange(event: any) {
    const file = event.target.files[0];

    if (file) {
      this.selectedAvatarFile = file;

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };

      reader.readAsDataURL(file);
    }
  }

  cleanUp() {
    this.imagePreview = null;
    this.selectedAvatarFile = undefined;
    this.selectedCustomer = this.emptyCustomer;
  }

  // Add Customer Functionality
  onAddFormSubmit() {
    if (this.addForm?.valid) {
      this.addCustomer();
    }
  }

  openAddFormDialog() {
    this.isAddCusFormOpen = true;
  }

  closeAddFormDialog() {
    this.cleanUp();
    this.isAddCusFormOpen = false;
  }

  // Edit Customer Functionality
  openEditFormDialog(cus: Customer) {
    this.selectedCustomer = cus;
    this.isEditCusFormOpen = true;
  }

  closeEditFormDialog() {
    this.cleanUp();
    this.isEditCusFormOpen = false;
  }

  onEditFormSubmit() {
    if (this.editForm?.valid) {
      this.updateCustomer();
    }
  }

  // Delete Customer Functionality
  openDeleteConfirmDialog(cus: Customer) {
    this.isConfirmDialogOpen = true;
    this.selectedCustomer = cus;
  }

  closeDialogConfirmDialog() {
    this.cleanUp();
    this.isConfirmDialogOpen = false;
  }

  formatDate(dateString: string): Date {
    let current = new Date(dateString);
    return new Date(
      current.getFullYear(),
      current.getMonth(),
      current.getDate()
    );
  }

  fetchCustomer() {
    this.customerService.fetchCustomer().subscribe(
      (response: any) => {
        this.customers = response.data.map((customer: Customer) => {
          return {
            ...customer,
            lastOrderDate:
              customer.lastOrderDate != null &&
              moment(customer.lastOrderDate).format('D MMMM YYYY HH:MM'),
          };
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  addCustomer() {
    const formData = new FormData();
    formData.append('customerName', this.addForm?.value.customerName);
    formData.append('customerAddress', this.addForm?.value.customerAddress);
    formData.append('customerPhone', this.addForm?.value.customerPhone);

    if (this.selectedAvatarFile != undefined) {
      formData.append(
        'pic',
        this.selectedAvatarFile,
        this.selectedAvatarFile?.name
      );
    }

    this.customerService.addNewCustomer(formData).subscribe(
      (response: any) => {
        this.fetchCustomer();
        this.isAddCusFormOpen = false;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  updateCustomer() {
    const formData = new FormData();
    formData.append('customerName', this.editForm?.value.customerName);
    formData.append('customerAddress', this.editForm?.value.customerAddress);
    formData.append('customerPhone', this.editForm?.value.customerPhone);

    if (this.selectedAvatarFile != undefined) {
      formData.append(
        'pic',
        this.selectedAvatarFile,
        this.selectedAvatarFile?.name
      );
    }

    this.customerService
      .updateCustomer(this.selectedCustomer.customerId, formData)
      .subscribe(
        (response: any) => {
          this.fetchCustomer();
          this.isEditCusFormOpen = false;
        },
        (error) => {
          console.log(error);
        }
      );
  }

  deleteCustomer() {
    if (this.selectedCustomer != undefined) {
      this.customerService
        .deleteCustoemer(this.selectedCustomer.customerId)
        .subscribe(
          (response: any) => {
            this.fetchCustomer();
          },
          (error) => {
            console.log(error);
          }
        );
    } else {
      console.log('Selected Customer is undefined');
    }
    this.isConfirmDialogOpen = false;
  }
}
