import { Component, OnInit } from '@angular/core';
import { Customer } from '../../../tools/typedef';
import { CustomerService } from '../../service/customer.service';
import moment from 'moment';
import { NgIf } from '@angular/common';
import { DialogConfimationComponent } from '../dialog-confimation/dialog-confimation.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [NgIf, DialogConfimationComponent, FormsModule],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css',
})
export class CustomerComponent implements OnInit {
  customers: Customer[] = [];
  isDialogOpen: boolean = false;
  isFormOpen: boolean = true;
  selectedCustomer: number | undefined;

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.fetchCustomer();
  }

  openDialog(cusId: number) {
    this.isDialogOpen = true;
    this.selectedCustomer = cusId;
  }

  closeDialog() {
    this.isDialogOpen = false;
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

  deleteCustomer() {
    if (this.selectedCustomer != undefined) {
      this.customerService.deleteCustoemer(this.selectedCustomer).subscribe(
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
    this.isDialogOpen = false;
  }
}
