import { Component, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-pagination-navigator',
  standalone: true,
  imports: [],
  templateUrl: './pagination-navigator.component.html',
  styleUrl: './pagination-navigator.component.css',
})
export class PaginationNavigatorComponent {
  @Input() pageCount: number = 0;
  @Input() activePage: number = 0;
  @Output() nextPageEvent = new EventEmitter<void>();
  @Output() prevPageEvent = new EventEmitter<void>();
  @Output() pageClickedEvent = new EventEmitter<number>();

  nextPage() {
    this.nextPageEvent.emit();
  }

  prevPage() {
    this.prevPageEvent.emit();
  }

  changeActivePage(page: number) {
    this.pageClickedEvent.emit(page);
  }
}
