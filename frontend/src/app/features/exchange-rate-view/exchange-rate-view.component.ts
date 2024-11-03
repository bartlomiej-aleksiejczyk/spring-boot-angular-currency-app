import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ApiService } from '../../core/services/api.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-exchange-rate-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './exchange-rate-view.component.html',
  styleUrl: './exchange-rate-view.component.scss',
})
export class ExchangeRateViewComponent {
  private apiService = inject(ApiService);
  currency = '';
  name = '';
  exchangeRate?: number;

  getExchangeRate() {
    this.apiService
      .getCurrentCurrencyValue({ currency: this.currency, name: this.name })
      .subscribe({
        next: (response) => {
          this.exchangeRate = response.value;
        },
        error: (error) => {
          console.error('Error fetching exchange rate', error);
        },
      });
  }
}
