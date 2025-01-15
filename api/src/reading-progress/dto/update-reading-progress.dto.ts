import { PartialType } from '@nestjs/swagger';
import { CreateReadingProgressDto } from './create-reading-progress.dto';

export class UpdateReadingProgressDto extends PartialType(CreateReadingProgressDto) {}
