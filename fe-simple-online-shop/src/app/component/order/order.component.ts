import { Component, ViewChild } from '@angular/core';
import { Customer, CustomerData, Item, Order } from '../../../tools/typedef';
import { OrderService } from '../../service/order.service';
import moment from 'moment';
import { NgIf } from '@angular/common';
import { DialogConfimationComponent } from '../dialog-confimation/dialog-confimation.component';
import { response } from 'express';
import { error } from 'console';
import { FormsModule, NgForm } from '@angular/forms';
import { CustomerService } from '../../service/customer.service';
import { ItemService } from '../../service/item.service';
import { rupiahFormat } from '../../../tools/const';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [NgIf, DialogConfimationComponent, FormsModule],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css',
})
export class OrderComponent {
  orders: Order[] = [];
  customers: CustomerData[] = [];
  items: Item[] = [];

  customerSelectedDV: number = -1;
  itemSelectedDV: number = -1;

  isConfirmDialogOpen: boolean = false;
  isAddOrderFormOpen: boolean = false;
  isEditOrderFormOpen: boolean = false;

  emtpyOrder: Order = {
    orderId: -1,
    orderCode: '',
    orderDate: new Date(),
    totalPrice: -1,
    quantity: -1,
    customer: null,
    item: null,
  };

  selectedOrder: Order = this.emtpyOrder;

  constructor(
    private orderService: OrderService,
    private customerService: CustomerService,
    private itemService: ItemService
  ) {}

  @ViewChild('addForm') addForm: NgForm | undefined;
  @ViewChild('editForm') editForm: NgForm | undefined;

  ngOnInit(): void {
    this.fetchAllOrder();
    this.fetchCustomer();
    this.fetchAvailableItem();
    // console.log(this.orders);
  }

  cleanUp() {
    this.customerSelectedDV = -1;
    this.itemSelectedDV = -1;
    this.selectedOrder = this.emtpyOrder;
  }

  openDialog(order: Order) {
    this.selectedOrder = order;
    this.isConfirmDialogOpen = true;
  }

  closeDialog() {
    this.isConfirmDialogOpen = false;
  }

  // Add Item Functionality
  onAddFormSubmit() {
    if (this.addForm?.valid) {
      this.addOrder();
      console.log(this.addForm.value);
    }
  }

  openAddFormDialog() {
    this.isAddOrderFormOpen = true;
  }

  closeAddFormDialog() {
    this.isAddOrderFormOpen = false;
  }

  // Edit Item Functionality
  openEditFormDialog(order: Order) {
    this.isEditOrderFormOpen = true;
    this.selectedOrder = order;
    this.itemSelectedDV = order.item?.itemId != null ? order.item.itemId : -1;
    this.customerSelectedDV =
      order.customer?.customerId != null ? order.customer.customerId : -1;
  }

  closeEditFormDialog() {
    this.cleanUp();
    this.isEditOrderFormOpen = false;
  }

  onEditFormSubmit() {
    if (this.editForm?.valid) {
      this.updateOrder();
    }
  }

  fetchAllOrder() {
    this.orderService.fetchAll().subscribe(
      (response: any) => {
        this.orders = response.data.map((order: Order) => {
          return {
            ...order,
            totalPrice: rupiahFormat.format(order.totalPrice),
            orderDate: moment(order.orderDate).format('D MMMM YYYY HH:MM'),
          };
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  fetchCustomer() {
    this.customerService.fetchCustomer().subscribe(
      (response: any) => {
        this.customers = response.data.map((customer: Customer) => {
          const customerData: CustomerData = {
            customerId: customer.customerId,
            customerName: customer.customerName,
          };

          return customerData;
        });

        this.customerSelectedDV = this.customers[0].customerId;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  fetchAvailableItem() {
    this.itemService.fetchAll().subscribe(
      (response: any) => {
        this.items = response.data.filter((item: Item) => item.isAvailable);
        this.itemSelectedDV = this.items[0].itemId;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  findItemQuantityById(id: number): number {
    const item = this.items.find((item) => item.itemId == id);
    if (item != undefined) {
      return item.stock;
    }
    return 0;
  }

  addOrder() {
    const formData = new FormData();
    formData.append('quantity', this.addForm?.value.quantity);
    formData.append('customerId', this.addForm?.value.customer);
    formData.append('itemId', this.addForm?.value.item);

    this.orderService.addNewOrder(formData).subscribe(
      (response: any) => {
        this.fetchAllOrder();
        this.isAddOrderFormOpen = false;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  updateOrder() {
    const formData = new FormData();
    formData.append('quantity', this.editForm?.value.quantity);
    formData.append('itemId', this.editForm?.value.item);

    this.orderService
      .updateOrder(this.selectedOrder.orderId, formData)
      .subscribe(
        (response: any) => {
          this.fetchAllOrder();
          this.isEditOrderFormOpen = false;
        },
        (error) => {
          console.log(error);
        }
      );
  }

  deleteOrder() {
    if (this.selectedOrder != undefined) {
      this.orderService.deleteOrder(this.selectedOrder.orderId).subscribe(
        (response: any) => {
          this.fetchAllOrder();
        },
        (error) => {
          console.log(error);
        }
      );
    } else {
      console.log('Selected Order is undefined');
    }
    this.isConfirmDialogOpen = false;
  }

  generateReport() {
    this.orderService.getReport().subscribe(
      (response: any) => {
        console.log(response.data);
        const url = window.URL.createObjectURL(
          new Blob([response], { type: 'application/pdf' })
        );
        const a = document.createElement('a');
        a.href = url;
        a.download = 'report.pdf';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
