import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environment/environment';
import { Observable } from 'rxjs';

interface ExchangeRateRequestDTO {
  currency: string;
  name: string;
}

interface ExchangeRateResponseDTO {
  value: number;
}

interface ExchangeRateQueryDTO {
  currency: string;
  name: string;
  date: string;
  value: number;
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getCurrentCurrencyValue(
    request: ExchangeRateRequestDTO
  ): Observable<ExchangeRateResponseDTO> {
    return this.http.post<ExchangeRateResponseDTO>(
      `${this.apiUrl}/currencies/get-current-currency-value-command`,
      request
    );
  }

  getAllCurrencyQueries(): Observable<ExchangeRateQueryDTO[]> {
    return this.http.get<ExchangeRateQueryDTO[]>(
      `${this.apiUrl}/currencies/requests`
    );
  }
  constructor() {}
}
