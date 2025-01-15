import { Body, Controller, Delete, FileTypeValidator, Get, HttpStatus, MaxFileSizeValidator, Param, ParseFilePipe, Post, Query, Req, Res, UploadedFile, UseGuards, UseInterceptors } from '@nestjs/common';
import { JwtGuard } from 'src/auth/guard/jwt.guard';
import { ErrorHandlerService } from 'src/common/utils/error-handler/error-handler.service';
import { FileService } from './file.service';
import { Response } from 'express';
import { FileInterceptor } from '@nestjs/platform-express';
import { UploadService } from 'src/upload1/upload.service';
import { v4 } from 'uuid';
import { Roles } from 'src/auth/decorator/roles.decorator';
import { StringService } from 'src/common/utils/string/string.service';
import { CreateFileDto } from './file.dto';
import { CommonUtilsFileService } from 'src/common/utils/common-utils-file/common-utils-file.service';
import { RolesGuard } from 'src/auth/guard/roles.guard';

@Controller('files')
export class FileController {

    constructor(
        private readonly filesService: FileService,
        private readonly errorHandlerService: ErrorHandlerService,
        private readonly uploadService: UploadService,
        private readonly stringService: StringService,
        private readonly commonUtilsfileService: CommonUtilsFileService
    ) { }

    private extractExtension(filename: string): string {
        const match = filename.match(/\.(jpeg|jpg|pdf|png)$/i);
        if (match) {
            return match[0].substring(1);
        }
        throw new Error('Unsupported file type');
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Get('download/:fileId')
    async downloadFile(
        @Param('fileId') fileId: string,
        @Req() req,
        @Res() res: Response
    ) {
        const file = await this.filesService.findUnique(fileId);
        if (file.userId !== req.user.id) {
            return res
                .status(HttpStatus.UNAUTHORIZED)
                .json({ message: 'Unauthorized' });
        }

        const filePath = this.stringService.purifyUrl(`${req.user.id}${file.relativeDirectoryPath}${fileId}.${file.type}`);
        const fileStream = await this.uploadService.getFileStream(filePath);
        const mimeType = this.commonUtilsfileService.getMimeType(file.type);

        res.set({
            'Content-Type': mimeType,
            'Content-Disposition': `attachment; filename="${file.name}.${file.type}"`
        });

        fileStream.pipe(res);
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Post('directory')
    public async createDirectory(
        @Req() req,
        @Body() dto: { relativeDirectoryPath: string },
        @Res() res: Response
    ): Promise<Response> {
        try {
            if (!dto.relativeDirectoryPath || dto.relativeDirectoryPath == '/' || dto.relativeDirectoryPath == '') {
                return res
                    .status(HttpStatus.BAD_REQUEST)
                    .json({ message: 'Empty directory name not allowed' });
            }
            const relativeDirectoryPath: string = dto.relativeDirectoryPath.endsWith('/') ? dto.relativeDirectoryPath : `${dto.relativeDirectoryPath}/`;
            const absoluteDirectoryPath: string = this.stringService.purifyUrl(`${req.user.id}/${relativeDirectoryPath}/`);
            await this.uploadService.createDirectory(absoluteDirectoryPath);
            return res.status(HttpStatus.CREATED).json(absoluteDirectoryPath);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Get('directory')
    public async listContents(
        @Query('path') path: string,
        @Req() req,
        @Res() res: Response
    ): Promise<Response> {
        try {
            const absoluteDirectoryPath = this.stringService.purifyUrl(`${req.user.id}/${path}/`);
            const contents = await this.uploadService.listDirectoryContents(absoluteDirectoryPath);
            const contentsWithoutCurrentDirectory = contents.filter(item => item.Key !== absoluteDirectoryPath);
            return res
                .status(HttpStatus.OK)
                .json({ contents: contentsWithoutCurrentDirectory });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Delete('directory')
    public async deleteDirectory(
        @Query('path') path: string,
        @Req() req,
        @Res() res
    ): Promise<Response> {
        try {
            const absoluteDirectoryPath = this.stringService.purifyUrl(`${req.user.id}/${path}/`);
            await this.uploadService.deleteDirectory(absoluteDirectoryPath);
            const relativeDirectoryPath = this.stringService.purifyUrl(`/ ${path}`);
            await this.filesService.deleteFilesOfDeletedDirectory(req.user.id, relativeDirectoryPath);
            return res
                .status(HttpStatus.NO_CONTENT)
                .json();
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Post()
    @UseInterceptors(FileInterceptor('file'))
    public async createFile(
        @UploadedFile(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /.(pdf|jpeg|png)$/ })
                ]
            })
        ) attachment: Express.Multer.File,
        @Req() req,
        @Body() dto,
        @Res() res
    ): Promise<Response> {
        try {
            const { data, s3Key, fileExtension } = await this.awsS3Service.create(attachment);
            const dataFile: CreateFileDto = {
                id: s3Key,
                name: dto.name
            };
            const { file, statusCode } = await this.filesService.create(dataFile);
            return res.status(statusCode).json(file);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Delete(':fileId')
    public async deleteUnique(
        @Param('fileId') fileId: string,
        @Req() req,
        @Res() res
    ): Promise<Response> {
        try {
            const absoluteDirectoryPath = await this.filesService.getAbsoluteFilePath(req.user.id, fileId);
            await this.uploadService.delete(absoluteDirectoryPath);
            const { statusCode } = await this.filesService.delete(req.user.id, fileId);
            return res.status(statusCode).json();
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Delete()
    public async deleteMany(
        @Body() dto: { fileIds: string[] },
        @Req() req,
        @Res() res
    ): Promise<Response> {
        try {
            const fileIds = dto.fileIds;
            for (const fileId of fileIds) {
                const absoluteDirectoryPath = await this.filesService.getAbsoluteFilePath(req.user.id, fileId);
                await this.uploadService.delete(absoluteDirectoryPath);
                await this.filesService.delete(req.user.id, fileId);
            }
            return res.status(HttpStatus.OK).json();
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard, RolesGuard)
    @Roles('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')
    @Get(':fileId')
    public async findUnique(
        @Param('fileId') fileId: string,
        @Req() req,
        @Res() res
    ): Promise<Response> {
        try {
            const file = await this.filesService.findUnique(fileId);
            if (file.userId !== req.user.id) {
                return res
                    .status(HttpStatus.UNAUTHORIZED)
                    .json({ message: 'Unauthorized' });
            }
            return res
                .status(HttpStatus.OK)
                .json(file);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

}
