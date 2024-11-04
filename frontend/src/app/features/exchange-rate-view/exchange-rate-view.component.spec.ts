import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { of, throwError } from 'rxjs';

import { ExchangeRateViewComponent } from './exchange-rate-view.component';
import { ApiService } from '../../core/services/api.service';

describe('ExchangeRateViewComponent', () => {
  let component: ExchangeRateViewComponent;
  let fixture: ComponentFixture<ExchangeRateViewComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    apiServiceSpy = jasmine.createSpyObj('ApiService', [
      'getCurrentCurrencyValue',
    ]);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, ExchangeRateViewComponent],
      declarations: [],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy },
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExchangeRateViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty fields', () => {
    expect(component.form.get('currency')?.value).toBe('');
    expect(component.form.get('name')?.value).toBe('');
  });

  it('should require currency and name fields', () => {
    const currencyControl = component.form.get('currency');
    const nameControl = component.form.get('name');

    currencyControl?.setValue('');
    nameControl?.setValue('');

    expect(currencyControl?.hasError('required')).toBeTrue();
    expect(nameControl?.hasError('required')).toBeTrue();
  });

  it('should enable submit button if form is valid', () => {
    component.form.get('currency')?.setValue('USD');
    component.form.get('name')?.setValue('John Doe');

    fixture.detectChanges();
    const button = fixture.debugElement.query(
      By.css('button[type="submit"]')
    ).nativeElement;
    expect(button.disabled).toBeFalse();
  });
  it('should allow valid currency and name values', () => {
    const currencyControl = component.form.get('currency');
    const nameControl = component.form.get('name');

    currencyControl?.setValue('USD');
    nameControl?.setValue('John Doe');

    expect(currencyControl?.valid).toBeTrue();
    expect(nameControl?.valid).toBeTrue();
  });
  it('should call API and set exchangeRate on success', () => {
    component.form.get('currency')?.setValue('USD');
    component.form.get('name')?.setValue('Dollar');
    const mockResponse = { value: 1.2 };
    apiServiceSpy.getCurrentCurrencyValue.and.returnValue(of(mockResponse));

    component.getExchangeRate();
    expect(apiServiceSpy.getCurrentCurrencyValue).toHaveBeenCalledWith(
      component.form.value
    );
    expect(component.exchangeRate).toBe(1.2);
    expect(component.errorMessage).toBeUndefined();
  });

  it('should show error message if form is invalid', () => {
    component.form.get('currency')?.setValue('');
    component.getExchangeRate();
    expect(component.errorMessage).toBe(
      'Please fill out all fields correctly.'
    );
  });

  it('should show error message on API error', () => {
    component.form.get('currency')?.setValue('USD');
    component.form.get('name')?.setValue('Dollar');
    apiServiceSpy.getCurrentCurrencyValue.and.returnValue(
      throwError(() => ({
        error: { status: 404, detail: 'Currency not found' },
      }))
    );

    component.getExchangeRate();
    expect(component.errorMessage).toBe('Currency not found');
  });
});
