import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FailModalComponent } from './fail-modal.component';

describe('FailModalComponent', () => {
  let component: FailModalComponent;
  let fixture: ComponentFixture<FailModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FailModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FailModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
