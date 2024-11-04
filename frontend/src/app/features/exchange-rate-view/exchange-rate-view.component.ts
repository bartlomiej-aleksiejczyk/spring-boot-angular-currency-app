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
  loading = false;
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      currency: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(255),
          Validators.pattern('.*\\S.*'),
        ],
      ],
      name: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(255),
          Validators.pattern('.*\\S.*'),
        ],
      ],
    });
  }

  getExchangeRate() {
    if (this.form.valid) {
      this.loading = true;
      this.apiService.getCurrentCurrencyValue(this.form.value).subscribe({
        next: (response) => {
          this.exchangeRate = response.value;
          this.errorMessage = undefined;
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage =
            error.error?.status == '404'
              ? error.error?.detail
              : 'Error fetching exchange rate. Please try again.';
          this.loading = false;
        },
      });
    } else {
      this.errorMessage = 'Please fill out all fields correctly.';
    }
  }
}
