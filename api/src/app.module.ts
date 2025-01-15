import { Module } from '@nestjs/common';
import { AuthModule } from './auth/auth.module';
import { UserModule } from './user/user.module';
import { PrismaModule } from './prisma/prisma.module';
import { CommonModule } from './common/common.module';
import { ConfigModule } from '@nestjs/config';
import { ScheduleModule } from '@nestjs/schedule';
import { FileModule } from './file/file.module';
import { UploadModule } from './upload1/upload.module';
import { BookModule } from './book/book.module';
import { AuthModule } from './auth/auth.module';
import { AwsS3Module } from './aws-s3/aws-s3.module';
import { UploadModule } from './upload/upload.module';
import { LikeModule } from './like/like.module';
import { ReadingProgressModule } from './reading-progress/reading-progress.module';
import { PageModule } from './page/page.module';
import { BookModule } from './book/book.module';

@Module({
    imports: [
        AuthModule,
        UserModule,
        PrismaModule,
        CommonModule,
        ConfigModule.forRoot({
            isGlobal: true
        }),
        ScheduleModule.forRoot(),
        FileModule,
        UploadModule,
        BookModule,
        PageModule,
        ReadingProgressModule,
        LikeModule,
        AwsS3Module
    ],
    controllers: [],
    providers: []
})
export class AppModule { }
