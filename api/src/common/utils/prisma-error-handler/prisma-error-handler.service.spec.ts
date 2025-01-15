import { Test, TestingModule } from '@nestjs/testing';
import { PrismaErrorHandlerService } from './prisma-error-handler.service';

describe('PrismaErrorHandlerService', () => {
  let service: PrismaErrorHandlerService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [PrismaErrorHandlerService]
    }).compile();

    service = module.get<PrismaErrorHandlerService>(PrismaErrorHandlerService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});
