import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyNowViewComponent } from './buy-now-view.component';

describe('BuyNowViewComponent', () => {
  let component: BuyNowViewComponent;
  let fixture: ComponentFixture<BuyNowViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuyNowViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuyNowViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
