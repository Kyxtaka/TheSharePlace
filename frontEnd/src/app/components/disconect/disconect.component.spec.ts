import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisconectComponent } from './disconect.component';

describe('DisconectComponent', () => {
  let component: DisconectComponent;
  let fixture: ComponentFixture<DisconectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisconectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DisconectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
