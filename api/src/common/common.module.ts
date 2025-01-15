import { Global, Module } from '@nestjs/common';
import { PrismaErrorHandlerService } from './utils/prisma-error-handler/prisma-error-handler.service';
import { ErrorHandlerService } from './utils/error-handler/error-handler.service';
import { DateService } from './utils/date/date.service';
import { StringService } from './utils/string/string.service';
import { CommonUtilsFileService } from './utils/common-utils-file/common-utils-file.service';

@Global()
@Module({
  providers: [PrismaErrorHandlerService, ErrorHandlerService, DateService, StringService, CommonUtilsFileService],
  exports: [PrismaErrorHandlerService, ErrorHandlerService, DateService, StringService, CommonUtilsFileService]
})
export class CommonModule { }
