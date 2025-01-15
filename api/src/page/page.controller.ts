import { Controller, Get, Post, Body, Patch, Param, Delete, UseInterceptors, UseGuards, UploadedFile, ParseFilePipe, MaxFileSizeValidator, FileTypeValidator, Req, Res } from '@nestjs/common';
import { PageService } from './page.service';
import { CreatePageDto } from './dto/create-page.dto';
import { UpdatePageDto } from './dto/update-page.dto';
import { Response } from 'express';
import { FileInterceptor } from '@nestjs/platform-express';
import { JwtGuard } from 'src/auth/guard/jwt.guard';
import { AwsS3Service } from 'src/aws-s3/aws-s3.service';
import { StringService } from 'src/common/utils/string/string.service';
import { ErrorHandlerService } from 'src/common/utils/error-handler/error-handler.service';
import { CommonUtilsFileService } from 'src/common/utils/common-utils-file/common-utils-file.service';

@Controller('page')
export class PageController {
    public constructor(
        private readonly pageService: PageService,
        private readonly errorHandlerService: ErrorHandlerService,
        private readonly awsS3Service: AwsS3Service,
        private readonly stringService: StringService,
        private readonly commonUtilsfileService: CommonUtilsFileService
    ) { }

    @UseGuards(JwtGuard)
    @Post()
    @UseInterceptors(FileInterceptor('file'))
    public async createPage(
        @UploadedFile(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /.(jpeg|jpg|pdf|png)$/ })
                ]
            })
        ) file: Express.Multer.File,
        @Req() req,
        @Body() dto,
        @Res() res
    ): Promise<Response> {
        try {
            const { data, statusCode } = await this.awsS3Service.uploadFile(file);
            const dataFile: CreatePageDto = {
                id: data.s3Key,
                name: dto.name
            };
            const { file, statusCode } = await this.pageService.createPage(dataFile);
            return res.status(statusCode).json(file);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @Get()
    findAll() {
        return this.pageService.findAll();
    }

    @Get(':id')
    findOne(@Param('id') id: string) {
        return this.pageService.findOne(+id);
    }

    @Patch(':id')
    update(@Param('id') id: string, @Body() updatePageDto: UpdatePageDto) {
        return this.pageService.update(+id, updatePageDto);
    }

    @Delete(':id')
    remove(@Param('id') id: string) {
        return this.pageService.remove(+id);
    }
}
