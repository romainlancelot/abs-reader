import { Controller, Get, Post, Body, Patch, Param, Delete } from '@nestjs/common';
import { ReadingProgressService } from './reading-progress.service';
import { CreateReadingProgressDto } from './dto/create-reading-progress.dto';
import { UpdateReadingProgressDto } from './dto/update-reading-progress.dto';

@Controller('reading-progress')
export class ReadingProgressController {
  constructor(private readonly readingProgressService: ReadingProgressService) {}

  @Post()
  create(@Body() createReadingProgressDto: CreateReadingProgressDto) {
    return this.readingProgressService.create(createReadingProgressDto);
  }

  @Get()
  findAll() {
    return this.readingProgressService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.readingProgressService.findOne(+id);
  }

  @Patch(':id')
  update(@Param('id') id: string, @Body() updateReadingProgressDto: UpdateReadingProgressDto) {
    return this.readingProgressService.update(+id, updateReadingProgressDto);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.readingProgressService.remove(+id);
  }
}
