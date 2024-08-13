import { Routes } from '@angular/router';
import { CustomerComponent } from './component/customer/customer.component';
import { ItemComponent } from './component/item/item.component';
import { OrderComponent } from './component/order/order.component';

export const routes: Routes = [
  { path: '', component: CustomerComponent },
  { path: 'customer', component: CustomerComponent },
  { path: 'item', component: ItemComponent },
  { path: 'order', component: OrderComponent },
];
