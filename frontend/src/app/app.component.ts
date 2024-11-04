import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { ExchangeRateViewComponent } from './features/exchange-rate-view/exchange-rate-view.component';
import { AppQueriesViewComponent } from './features/app-queries-view/app-queries-view.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ExchangeRateViewComponent, AppQueriesViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'Currency Info';
  activeTab: 'exchangeRate' | 'queries' = 'exchangeRate';

  showExchangeRate() {
    this.activeTab = 'exchangeRate';
  }

  showQueries() {
    this.activeTab = 'queries';
  }
}
