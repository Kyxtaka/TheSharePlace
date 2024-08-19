import { TestBed } from '@angular/core/testing';

import { TokenUtilService } from './token-util.service';

describe('TokenUtilService', () => {
  let service: TokenUtilService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenUtilService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
