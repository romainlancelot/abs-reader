import { Injectable } from "@nestjs/common";
import { ConfigService } from "@nestjs/config";
import { S3Client, PutObjectCommand, DeleteObjectCommand } from "@aws-sdk/client-s3";
import { v4 } from "uuid";

@Injectable()
export class AwsS3Service {
    private readonly AWS_S3_REGION: string = this.configService.getOrThrow("AWS_S3_REGION");
    private readonly AWS_S3_BUCKET_NAME: string = this.configService.getOrThrow("AWS_S3_BUCKET_NAME");
    private readonly s3Client: S3Client = new S3Client({ region: this.AWS_S3_REGION });

    public constructor(
        private readonly configService: ConfigService
    ) { }

    public async upload(
        pngFileBuffer: Buffer
    ): Promise<{ fileId: string, fileUrl: string }> {
        try {
            const key: string = v4();
            const fileId: string = `${key}.png`;
            const fileUrl: string = `https://${this.AWS_S3_BUCKET_NAME}.s3.${this.AWS_S3_REGION}.amazonaws.com/${fileId}`;
            const command: PutObjectCommand = new PutObjectCommand({
                Bucket: this.AWS_S3_BUCKET_NAME,
                Key: fileId,
                Body: pngFileBuffer,
                ContentType: "image/png"
            });
            await this.s3Client.send(command);
            return { fileId, fileUrl };
        } catch (error: any) {
            console.error(error.message);
            throw error;
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
        } catch (error: any) {
            console.error(error.message);
            throw error;
        }
    }
}
