import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';

import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-exchange-rate-view',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './exchange-rate-view.component.html',
  styleUrl: './exchange-rate-view.component.scss',
})
export class ExchangeRateViewComponent {
  private apiService = inject(ApiService);
  exchangeRate?: number;
  errorMessage?: string;
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      currency: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(255),
        ],
      ],
      name: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(255),
        ],
      ],
    });
  }

  getExchangeRate() {
    if (this.form.valid) {
      this.apiService.getCurrentCurrencyValue(this.form.value).subscribe({
        next: (response) => {
          this.exchangeRate = response.value;
          this.errorMessage = undefined;
        },
        error: (error) => {
          if (error.error?.status == '404') {
            this.errorMessage = error.error?.detail;
          } else {
            this.errorMessage =
              'Error fetching exchange rate. Please try again.';
          }

          console.error('Error:', error);
        },
      });
    } else {
      this.errorMessage = 'Please fill out all fields correctly.';
    }
  }
}
