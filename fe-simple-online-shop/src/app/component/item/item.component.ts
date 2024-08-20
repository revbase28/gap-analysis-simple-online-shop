import { Component, OnInit, ViewChild } from '@angular/core';
import { Item } from '../../../tools/typedef';
import { ItemService } from '../../service/item.service';
import moment from 'moment';
import { DialogConfimationComponent } from '../dialog-confimation/dialog-confimation.component';
import { NgIf } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { rupiahFormat } from '../../../tools/const';
import { PaginationNavigatorComponent } from '../pagination-navigator/pagination-navigator.component';
import { SearchBarComponent } from '../search-bar/search-bar.component';

@Component({
  selector: 'app-item',
  standalone: true,
  imports: [
    NgIf,
    DialogConfimationComponent,
    FormsModule,
    PaginationNavigatorComponent,
    SearchBarComponent,
  ],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css',
})
export class ItemComponent implements OnInit {
  items: Item[] = [];
  totalPage: number = 0;
  activePage: number = 1;

  searchKeyword: string = '';

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

  fetchAllItem(page: number = 0) {
    this.itemService.fetchAll(page).subscribe(
      (response: any) => {
        this.items = response.data.content.map((item: Item) => {
          return {
            ...item,
            lastReStock: moment(item.lastReStock).format('D MMMM YYYY HH:MM'),
          };
        });

        this.totalPage = response.data.totalPages;
        this.activePage = response.data.pageable.pageNumber + 1;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  searchItem(page: number = 0) {
    this.itemService.searchItem(this.searchKeyword, page).subscribe(
      (response: any) => {
        this.items = response.data.content.map((item: Item) => {
          return {
            ...item,
            lastReStock: moment(item.lastReStock).format('D MMMM YYYY HH:MM'),
          };
        });

        this.totalPage = response.data.totalPages;
        this.activePage = response.data.pageable.pageNumber + 1;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  formatToRupiah(price: number): string {
    return rupiahFormat.format(price);
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

  fetchWithCondition() {
    if (this.searchKeyword == '') {
      this.fetchAllItem(this.activePage - 1);
    } else {
      this.searchItem(this.activePage - 1);
    }
  }

  // Pagination Related
  nextPage() {
    if (this.activePage != this.totalPage) {
      this.activePage = this.activePage + 1;
      this.fetchWithCondition();
    }
  }

  prevPage() {
    if (this.activePage > 1) {
      this.activePage = this.activePage - 1;
      this.fetchWithCondition();
    }
  }

  changeActivePage(page: number) {
    this.activePage = page;
    this.fetchWithCondition();
  }

  // Search functionality
  onSearchEvent(keyword: string) {
    this.searchKeyword = keyword;
    this.searchItem();
  }

  onKeywordReset() {
    this.searchKeyword = '';
    this.fetchAllItem();
  }
}
