import { Component, OnInit, ViewChild } from '@angular/core';
import { Item } from '../../../tools/typedef';
import { ItemService } from '../../service/item.service';
import moment from 'moment';
import { DialogConfimationComponent } from '../dialog-confimation/dialog-confimation.component';
import { NgIf } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { rupiahFormat } from '../../../tools/const';

@Component({
  selector: 'app-item',
  standalone: true,
  imports: [NgIf, DialogConfimationComponent, FormsModule],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css',
})
export class ItemComponent implements OnInit {
  items: Item[] = [];
  isConfirmDialogOpen: boolean = false;
  isAddItemFormOpen: boolean = false;
  isEditItemFormOpen: boolean = false;

  emptyItem: Item = {
    itemId: -1,
    itemName: '',
    itemCode: '',
    price: -1,
    stock: -1,
    isAvailable: false,
    lastReStock: new Date(),
  };

  selectedItem: Item = this.emptyItem;

  constructor(private itemService: ItemService) {}

  @ViewChild('addForm') addForm: NgForm | undefined;
  @ViewChild('editForm') editForm: NgForm | undefined;

  ngOnInit(): void {
    this.fetchAllItem();
    console.log(this.items);
  }

  openDialog(item: Item) {
    this.isConfirmDialogOpen = true;
    this.selectedItem = item;
  }

  closeDialog() {
    this.selectedItem = this.emptyItem;
    this.isConfirmDialogOpen = false;
  }

  // Add Item Functionality
  onAddFormSubmit() {
    if (this.addForm?.valid) {
      this.addItem();
      // console.log(this.addForm.value);
    }
  }

  openAddFormDialog() {
    this.isAddItemFormOpen = true;
  }

  closeAddFormDialog() {
    this.selectedItem = this.emptyItem;
    this.isAddItemFormOpen = false;
  }

  // Edit Item Functionality
  openEditFormDialog(item: Item) {
    console.log(item);
    this.selectedItem = item;
    this.isEditItemFormOpen = true;
  }

  closeEditFormDialog() {
    this.selectedItem = this.emptyItem;
    this.isEditItemFormOpen = false;
  }

  onEditFormSubmit() {
    if (this.editForm?.valid) {
      this.updateItem();
    }
  }

  fetchAllItem() {
    this.itemService.fetchAll().subscribe(
      (response: any) => {
        this.items = response.data.map((item: Item) => {
          return {
            ...item,
            price: rupiahFormat.format(item.price),
            lastReStock: moment(item.lastReStock).format('D MMMM YYYY HH:MM'),
          };
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  addItem() {
    const formData = new FormData();
    formData.append('itemName', this.addForm?.value.itemName);
    formData.append('stock', this.addForm?.value.itemStock);
    formData.append('price', this.addForm?.value.itemPrice);

    this.itemService.addNewItem(formData).subscribe(
      (response: any) => {
        this.fetchAllItem();
        this.isAddItemFormOpen = false;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  updateItem() {
    const formData = new FormData();
    formData.append('itemName', this.editForm?.value.itemName);
    formData.append('stock', this.editForm?.value.itemStock);
    formData.append('price', this.editForm?.value.itemPrice);
    formData.append('isAvailable', this.editForm?.value.itemAvailability);

    this.itemService.updateItem(this.selectedItem.itemId, formData).subscribe(
      (response: any) => {
        this.fetchAllItem();
        this.isEditItemFormOpen = false;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  deleteItem() {
    if (this.selectedItem != undefined) {
      this.itemService.deleteItem(this.selectedItem.itemId).subscribe(
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
    this.isConfirmDialogOpen = false;
  }
}
