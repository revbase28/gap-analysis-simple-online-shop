import { NumberSymbol } from '@angular/common';

export interface Customer {
  customerId: number;
  customerName: string;
  customerAddress: string;
  customerCode: string;
  customerPhone: string;
  isActive: boolean;
  lastOrderDate: Date;
  pic: string;
}

export interface Item {
  itemId: number;
  itemName: string;
  itemCode: string;
  stock: number;
  price: number;
  isAvailable: boolean;
  lastReStock: Date;
}

export interface Order {
  orderId: number;
  orderCode: string;
  orderDate: Date;
  totalPrice: number;
  quantity: number;
  customer: Customer;
  item: Item;
}
