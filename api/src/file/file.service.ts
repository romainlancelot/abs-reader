import { HttpStatus, Injectable } from '@nestjs/common';
import { ErrorHandlerService } from 'src/common/utils/error-handler/error-handler.service';
import { PrismaService } from 'src/prisma/prisma.service';
import { CreateFileDto } from './file.dto';
import { File } from '@prisma/client';
import { StringService } from 'src/common/utils/string/string.service';

@Injectable()
export class FileService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly errorHandlerService: ErrorHandlerService,
        private readonly stringService: StringService
    ) { }

    public async create(
        dto: CreateFileDto
    ): Promise<{ file: File; statusCode: number }> {
        try {
            const file: File = await this.prisma.file.create({
                data: {
                    id: dto.id,
                    name: dto.name,
                    userId: dto.userId,
                    type: dto.type,
                    relativeDirectoryPath: this.stringService.purifyUrl(
                        '/' + dto.relativeDirectoryPath + '/'
                    )
                }
            });
            return { file, statusCode: 201 };
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    public async findUnique(id: string): Promise<File> {
        try {
            const file: File = await this.prisma.file.findUnique({
                where: { id }
            });
            return file;
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
    public async findMany(userId: string): Promise<File[]> {
        try {
            return await this.prisma.file.findMany({
                where: {
                    userId
                }
            });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    public async getAbsoluteFilePath(
        userId: string,
        fileId: string
    ): Promise<string> {
        try {
            const file = await this.prisma.file.findUnique({ where: { id: fileId } });
            if (file.userId !== userId) {
                throw new Error('Unauthorized');
            } else {
                return await this.stringService.purifyUrl(
                    file.userId +
                    '/' +
                    file.relativeDirectoryPath +
                    '/' +
                    file.id +
                    '.' +
                    file.type
                );
            }
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    public async delete(
        userId: string,
        fileId: string
    ): Promise<{ statusCode: number }> {
        try {
            const file = await this.prisma.file.findUnique({ where: { id: fileId } });
            if (file.userId !== userId) {
                throw new Error('Unauthorized');
            } else {
                await this.prisma.file.delete({ where: { id: fileId } });
                return { statusCode: HttpStatus.NO_CONTENT };
            }
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    public async deleteFilesOfDeletedDirectory(
        userId: string,
        path: string
    ): Promise<void> {
        try {
            await this.prisma.file.deleteMany({
                where: {
                    userId: userId,
                    relativeDirectoryPath: {
                        startsWith: path
                    }
                }
            });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
