import { Test, TestingModule } from "@nestjs/testing";
import { CommonUtilsFileService } from "./common-utils-file.service";

describe("CommonUtilsFileService", () => {
  let service: CommonUtilsFileService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [CommonUtilsFileService]
    }).compile();

    service = module.get<CommonUtilsFileService>(CommonUtilsFileService);
  });

  it("should be defined", () => {
    expect(service).toBeDefined();
  });
});
