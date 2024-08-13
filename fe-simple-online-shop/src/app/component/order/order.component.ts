import { Component } from '@angular/core';
import { Order } from '../../../tools/typedef';
import { OrderService } from '../../service/order.service';
import moment from 'moment';
import { NgIf } from '@angular/common';
import { DialogConfimationComponent } from '../dialog-confimation/dialog-confimation.component';
import { response } from 'express';
import { error } from 'console';

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [NgIf, DialogConfimationComponent],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css',
})
export class OrderComponent {
  orders: Order[] = [];
  isDialogOpen: boolean = false;
  selectedOrder: number | undefined = undefined;

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.fetchAllOrder();
    console.log(this.orders);
  }

  openDialog(orderId: number) {
    this.selectedOrder = orderId;
    this.isDialogOpen = true;
  }

  closeDialog() {
    this.isDialogOpen = false;
  }

  fetchAllOrder() {
    this.orderService.fetchAll().subscribe(
      (response: any) => {
        this.orders = response.data.map((order: Order) => {
          return {
            ...order,
            orderDate: moment(order.orderDate).format('D MMMM YYYY HH:MM'),
          };
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  deleteOrder() {
    if (this.selectedOrder != undefined) {
      this.orderService.deleteOrder(this.selectedOrder).subscribe(
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
    this.isDialogOpen = false;
  }
}
