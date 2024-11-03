import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExchangeRateViewComponent } from './exchange-rate-view.component';

describe('ExchangeRateViewComponent', () => {
  let component: ExchangeRateViewComponent;
  let fixture: ComponentFixture<ExchangeRateViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExchangeRateViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExchangeRateViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
