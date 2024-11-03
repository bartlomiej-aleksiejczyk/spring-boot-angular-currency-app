import { Component, OnInit, inject } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

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

  ngOnInit() {
    this.apiService.getAllCurrencyQueries().subscribe({
      next: (data) => {
        this.queries = data;
      },
      error: (error) => {
        console.error('Error fetching queries', error);
      },
    });
  }
}
