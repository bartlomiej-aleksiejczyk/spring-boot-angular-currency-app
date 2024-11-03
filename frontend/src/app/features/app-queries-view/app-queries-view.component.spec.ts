import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppQueriesViewComponent } from './app-queries-view.component';

describe('AppQueriesViewComponent', () => {
  let component: AppQueriesViewComponent;
  let fixture: ComponentFixture<AppQueriesViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppQueriesViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AppQueriesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
