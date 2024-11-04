import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';
import { of, throwError } from 'rxjs';

import { ApiService } from '../../core/services/api.service';
import { AppQueriesViewComponent } from './app-queries-view.component';

describe('AppQueriesViewComponent', () => {
  let component: AppQueriesViewComponent;
  let fixture: ComponentFixture<AppQueriesViewComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    apiServiceSpy = jasmine.createSpyObj('ApiService', [
      'getAllCurrencyQueries',
    ]);
    apiServiceSpy.getAllCurrencyQueries.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [AppQueriesViewComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceSpy },
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppQueriesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render empty message when emptyMessage is set', () => {
    component.emptyMessage = 'No queries found.';
    fixture.detectChanges();

    const emptyMessageElement = fixture.debugElement.query(
      By.css('.query-container p')
    );
    expect(emptyMessageElement.nativeElement.textContent).toContain(
      'No queries found.'
    );
  });
  it('should render error message when errorMessage is set', () => {
    component.errorMessage = 'Unable to fetch queries. Please try again later.';
    fixture.detectChanges();

    const errorElement = fixture.debugElement.query(By.css('.error'));
    expect(errorElement.nativeElement.textContent).toContain(
      'Unable to fetch queries. Please try again later.'
    );
  });
  it('should call fetchQueries on ngOnInit', () => {
    spyOn(component, 'fetchQueries');
    component.ngOnInit();
    expect(component.fetchQueries).toHaveBeenCalled();
  });
  it('should display errorMessage if fetch fails', () => {
    apiServiceSpy.getAllCurrencyQueries.and.returnValue(
      throwError(() => new Error('Fetch error'))
    );
    component.fetchQueries();
    expect(component.loading).toBeFalse();
    expect(component.errorMessage).toBe(
      'Unable to fetch queries. Please try again later.'
    );
    expect(component.queries).toEqual([]);
  });
  it('should display emptyMessage if no queries are found', () => {
    apiServiceSpy.getAllCurrencyQueries.and.returnValue(of([]));

    component.fetchQueries();
    expect(component.loading).toBeFalse();
    expect(component.queries).toEqual([]);
    expect(component.emptyMessage).toBe('No queries found.');
  });

  it('should render loading spinner when loading is true', () => {
    component.loading = true;
    fixture.detectChanges();

    const spinner = fixture.debugElement.query(By.css('.spinner'));
    expect(spinner).toBeTruthy();
  });
  it('should render queries table when data is available', () => {
    const mockData = [
      {
        currency: 'USD',
        name: 'John Doe',
        date: '2025-12-19T00:00:00Z',
        value: 1.23,
      },
    ];

    apiServiceSpy.getAllCurrencyQueries.and.returnValue(of(mockData));

    component.fetchQueries();
    fixture.detectChanges();

    const tableRows = fixture.debugElement.queryAll(By.css('table tbody tr'));
    expect(tableRows.length).toBe(mockData.length);
  });
});
