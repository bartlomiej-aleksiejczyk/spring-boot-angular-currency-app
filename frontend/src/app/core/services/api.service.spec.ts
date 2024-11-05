import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import {
  ExchangeRateRequestDTO,
  ExchangeRateResponseDTO,
  ExchangeRateQueryDTO,
  ApiService,
} from './api.service';
import { environment } from '../../../environment/environment';

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;
  const apiUrl = environment.apiUrl;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getCurrentCurrencyValue', () => {
    it('should make a POST request and return expected response', () => {
      const request: ExchangeRateRequestDTO = {
        currency: 'USD',
        name: 'Dollar',
      };
      const mockResponse: ExchangeRateResponseDTO = { value: 1.2 };

      service.getCurrentCurrencyValue(request).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(
        `${apiUrl}/currencies/get-current-currency-value-command`
      );
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(request);

      req.flush(mockResponse);
    });
  });

  describe('getAllCurrencyQueries', () => {
    it('should make a GET request and return a list of currency queries', () => {
      const mockResponse: ExchangeRateQueryDTO[] = [
        { currency: 'USD', name: 'Dollar', date: '2023-11-01', value: 1.2 },
        { currency: 'EUR', name: 'Euro', date: '2023-11-01', value: 0.9 },
      ];

      service.getAllCurrencyQueries().subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${apiUrl}/currencies/requests`);
      expect(req.request.method).toBe('GET');

      req.flush(mockResponse);
    });
  });
});
