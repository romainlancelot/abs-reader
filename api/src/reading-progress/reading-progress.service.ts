import { Injectable } from "@nestjs/common";
import { CreateReadingProgressDto } from "./dto/create-reading-progress.dto";
import { UpdateReadingProgressDto } from "./dto/update-reading-progress.dto";

@Injectable()
export class ReadingProgressService {
  create(createReadingProgressDto: CreateReadingProgressDto) {
    return "This action adds a new readingProgress";
  }

  findAll() {
    return "This action returns all readingProgress";
  }

  findOne(id: number) {
    return `This action returns a #${id} readingProgress`;
  }

  update(id: number, updateReadingProgressDto: UpdateReadingProgressDto) {
    return `This action updates a #${id} readingProgress`;
  }

  remove(id: number) {
    return `This action removes a #${id} readingProgress`;
  }
}
