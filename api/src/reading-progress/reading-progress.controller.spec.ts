import { Test, TestingModule } from "@nestjs/testing";
import { ReadingProgressController } from "./reading-progress.controller";
import { ReadingProgressService } from "./reading-progress.service";

describe("ReadingProgressController", () => {
  let controller: ReadingProgressController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [ReadingProgressController],
      providers: [ReadingProgressService]
    }).compile();

    controller = module.get<ReadingProgressController>(ReadingProgressController);
  });

  it("should be defined", () => {
    expect(controller).toBeDefined();
  });
});
