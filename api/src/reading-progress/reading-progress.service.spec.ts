import { Test, TestingModule } from '@nestjs/testing';
import { ReadingProgressService } from './reading-progress.service';

describe('ReadingProgressService', () => {
  let service: ReadingProgressService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [ReadingProgressService]
    }).compile();

    service = module.get<ReadingProgressService>(ReadingProgressService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});
