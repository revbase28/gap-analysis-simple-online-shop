import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogConfimationComponent } from './dialog-confimation.component';

describe('DialogConfimationComponent', () => {
  let component: DialogConfimationComponent;
  let fixture: ComponentFixture<DialogConfimationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogConfimationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogConfimationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
