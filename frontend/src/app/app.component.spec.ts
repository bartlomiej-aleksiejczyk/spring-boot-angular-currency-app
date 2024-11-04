import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { AppQueriesViewComponent } from './features/app-queries-view/app-queries-view.component';
import { ExchangeRateViewComponent } from './features/exchange-rate-view/exchange-rate-view.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'Currency Info' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('Currency Info');
  });
  it('should not render AppQueriesViewComponent when activeTab is "exchangeRate"', () => {
    const fixture = TestBed.createComponent(AppComponent);

    fixture.componentInstance.activeTab = 'exchangeRate';
    fixture.detectChanges();

    const queriesComponent = fixture.debugElement.query(
      By.directive(AppQueriesViewComponent)
    );
    expect(queriesComponent).toBeFalsy();
  });

  it('should not render ExchangeRateViewComponent when activeTab is "queries"', () => {
    const fixture = TestBed.createComponent(AppComponent);

    fixture.componentInstance.activeTab = 'queries';
    fixture.detectChanges();

    const exchangeRateComponent = fixture.debugElement.query(
      By.directive(ExchangeRateViewComponent)
    );
    expect(exchangeRateComponent).toBeFalsy();
  });
  it('should set activeTab to "exchangeRate" when showExchangeRate is called', () => {
    const fixture = TestBed.createComponent(AppComponent);

    fixture.componentInstance.showExchangeRate();
    expect(fixture.componentInstance.activeTab).toBe('exchangeRate');
  });

  it('should render ExchangeRateViewComponent when activeTab is "exchangeRate"', () => {
    const fixture = TestBed.createComponent(AppComponent);

    fixture.componentInstance.activeTab = 'exchangeRate';
    fixture.detectChanges();

    const exchangeRateComponent = fixture.debugElement.query(
      By.directive(ExchangeRateViewComponent)
    );
    expect(exchangeRateComponent).toBeTruthy();
  });

  it('should render AppQueriesViewComponent when activeTab is "queries"', () => {
    const fixture = TestBed.createComponent(AppComponent);

    fixture.componentInstance.activeTab = 'queries';
    fixture.detectChanges();

    const queriesComponent = fixture.debugElement.query(
      By.directive(AppQueriesViewComponent)
    );
    expect(queriesComponent).toBeTruthy();
  });
});
