import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-queries-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app-queries-view.component.html',
  styleUrl: './app-queries-view.component.scss',
})
export class AppQueriesViewComponent implements OnInit {
  private apiService = inject(ApiService);
  queries: any[] = [];
  errorMessage?: string;
  emptyMessage?: string;
  loading = false;

  ngOnInit() {
    this.fetchQueries();
  }

  fetchQueries() {
    this.loading = true;
    this.errorMessage = undefined;
    this.emptyMessage = undefined;

    this.apiService.getAllCurrencyQueries().subscribe({
      next: (data) => {
        this.queries = data;
        if (this.queries.length === 0) {
          this.emptyMessage = 'No queries found.';
        }
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Unable to fetch queries. Please try again later.';
        console.error('Error fetching queries:', error);
        this.loading = false;
      },
    });
  }
}
