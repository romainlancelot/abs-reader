import { Module } from '@nestjs/common';
import { FileController } from './file.controller';
import { FileService } from './file.service';
import { UploadService } from 'src/upload1/upload.service';

@Module({
    controllers: [FileController],
    providers: [FileService, UploadService]
})
export class FileModule { }
