import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css',
})
export class SearchBarComponent {
  keyword: string = '';

  @Output() searchEvent = new EventEmitter<string>();
  @Output() keywordResetEvent = new EventEmitter<void>();

  onValueChange() {
    if (this.keyword == '') {
      this.keywordResetEvent.emit();
    }
  }

  onSearch() {
    this.searchEvent.emit(this.keyword);
  }
}
