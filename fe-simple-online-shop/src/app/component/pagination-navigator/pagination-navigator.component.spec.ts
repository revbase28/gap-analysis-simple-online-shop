import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginationNavigatorComponent } from './pagination-navigator.component';

describe('PaginationNavigatorComponent', () => {
  let component: PaginationNavigatorComponent;
  let fixture: ComponentFixture<PaginationNavigatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaginationNavigatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaginationNavigatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
