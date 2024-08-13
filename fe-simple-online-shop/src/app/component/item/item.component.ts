import { Component, OnInit } from '@angular/core';
import { Item } from '../../../tools/typedef';
import { ItemService } from '../../service/item.service';
import moment from 'moment';
import { DialogConfimationComponent } from '../dialog-confimation/dialog-confimation.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-item',
  standalone: true,
  imports: [NgIf, DialogConfimationComponent],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css',
})
export class ItemComponent implements OnInit {
  items: Item[] = [];
  isDialogOpen = false;
  selectedItem: number | undefined;

  constructor(private itemService: ItemService) {}

  ngOnInit(): void {
    this.fetchAllItem();
    console.log(this.items);
  }

  openDialog(itemId: number) {
    this.isDialogOpen = true;
    this.selectedItem = itemId;
  }

  closeDialog() {
    this.isDialogOpen = false;
  }

  fetchAllItem() {
    this.itemService.fetchAll().subscribe(
      (response: any) => {
        this.items = response.data.map((item: Item) => {
          return {
            ...item,
            lastReStock: moment(item.lastReStock).format('D MMMM YYYY HH:MM'),
          };
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  deleteItem() {
    if (this.selectedItem != undefined) {
      this.itemService.deleteItem(this.selectedItem).subscribe(
        (response: any) => {
          this.fetchAllItem();
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
