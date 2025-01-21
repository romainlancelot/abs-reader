import { Injectable } from "@nestjs/common";
import { ConfigService } from "@nestjs/config";
import { S3Client, PutObjectCommand, DeleteObjectCommand } from "@aws-sdk/client-s3";
import { v4 } from "uuid";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";

@Injectable()
export class AwsS3Service {
    private readonly AWS_S3_REGION: string = this.configService.getOrThrow("AWS_S3_REGION");
    private readonly AWS_S3_BUCKET_NAME: string = this.configService.getOrThrow("AWS_S3_BUCKET_NAME");
    private readonly s3Client: S3Client = new S3Client({ region: this.AWS_S3_REGION });

    public constructor(
        private readonly configService: ConfigService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    private extractExtension(filename: string): string {
        const match: RegExpMatchArray = filename.match(/\.(jpeg|jpg|pdf|png)$/i);
        if (match) {
            return match[0].substring(1);
        }
        return null;
    }

    public async upload(
        file: Express.Multer.File
    ): Promise<{ fileId: string, fileUrl: string }> {
        try {
            const key: string = v4();
            const fileExtension: string = this.extractExtension(file.originalname.toLowerCase());
            const fileId: string = `${key}.${fileExtension}`;
            const fileUrl: string = `https://${this.AWS_S3_BUCKET_NAME}.s3.${this.AWS_S3_REGION}.amazonaws.com/${fileId}`;
            const command: PutObjectCommand = new PutObjectCommand({
                Bucket: this.AWS_S3_BUCKET_NAME,
                Key: fileId,
                Body: file.buffer
            });

            await this.s3Client.send(command);
            return { fileId, fileUrl };
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    public async delete(
        fileId: string
    ): Promise<void> {
        try {
            const command: DeleteObjectCommand = new DeleteObjectCommand({
                Bucket: this.AWS_S3_BUCKET_NAME,
                Key: fileId
            });
            await this.s3Client.send(command);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
