import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-dialog-confimation',
  standalone: true,
  imports: [],
  templateUrl: './dialog-confimation.component.html',
  styleUrl: './dialog-confimation.component.css',
})
export class DialogConfimationComponent {
  @Input() message: string = '';
  @Output() confirmDeleteEvent = new EventEmitter<void>();
  @Output() cancelEvent = new EventEmitter<void>();

  onConfirm() {
    this.confirmDeleteEvent.emit();
  }

  onCancel() {
    this.cancelEvent.emit();
  }
}
