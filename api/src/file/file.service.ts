import { Injectable } from "@nestjs/common";
import { CreateFileDto } from "./dto/create-file.dto";
import { ConfigService } from "@nestjs/config";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { DeleteObjectCommand, PutObjectCommand, S3Client } from "@aws-sdk/client-s3";
import { v4 } from "uuid";
import { File } from "@prisma/client";
import { PrismaService } from "src/prisma/prisma.service";

@Injectable()
export class FileService {
    private readonly AWS_S3_REGION: string = this.configService.getOrThrow("AWS_S3_REGION");
    private readonly AWS_S3_BUCKET_NAME: string = this.configService.getOrThrow("AWS_S3_BUCKET_NAME");
    private readonly s3Client: S3Client = new S3Client({ region: this.AWS_S3_REGION });

    public constructor(
        private readonly configService: ConfigService,
        private readonly errorHandlerService: ErrorHandlerService,
        private readonly prisma: PrismaService
    ) { }

    private extractExtension(filename: string): string {
        const match: RegExpMatchArray = filename.match(/\.(jpeg|jpg|pdf|png)$/i);
        if (match) {
            return match[0].substring(1);
        }
        throw new Error("Unsupported file type.");
    }

    public async create(
        file: Express.Multer.File
    ): Promise<File> {
        try {
            const createFileDto: CreateFileDto = await this.upload(file);
            return await this.prisma.file.create({ data: createFileDto });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    private async upload(
        file: Express.Multer.File
    ): Promise<CreateFileDto> {
        try {
            const key: string = v4();
            const fileExtension: string = this.extractExtension(file.originalname.toLowerCase());
            const fileName: string = `${key}.${fileExtension}`;
            const command: PutObjectCommand = new PutObjectCommand({
                Bucket: this.AWS_S3_BUCKET_NAME,
                Key: fileName,
                Body: file.buffer
            });
            await this.s3Client.send(command);
            const createFileDto: CreateFileDto = {
                name: fileName,
                url: `https://${this.AWS_S3_BUCKET_NAME}.s3.${this.AWS_S3_REGION}.amazonaws.com/${fileName}`
            };
            return createFileDto;
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    public async deleteFile(
        fileId: string
    ): Promise<void> {
        try {
            const command: DeleteObjectCommand = new DeleteObjectCommand({
                Bucket: this.AWS_S3_BUCKET_NAME,
                Key: fileId
            });
            await this.s3Client.send(command);
            await this.prisma.file.delete({
                where: {
                    name: fileId
                }
            });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

}
