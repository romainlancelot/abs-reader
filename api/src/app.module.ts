import { Module } from "@nestjs/common";
import { AuthModule } from "./auth/auth.module";
import { UserModule } from "./user/user.module";
import { PrismaModule } from "./prisma/prisma.module";
import { CommonModule } from "./common/common.module";
import { ConfigModule } from "@nestjs/config";
import { ScheduleModule } from "@nestjs/schedule";
import { BookModule } from "./book/book.module";
import { AwsS3Module } from "./aws-s3/aws-s3.module";
import { LikeModule } from "./like/like.module";
import { ReadingProgressModule } from "./reading-progress/reading-progress.module";
import { PageModule } from "./page/page.module";
import { MulterModule } from "@nestjs/platform-express";
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
        BookModule,
        PageModule,
        ReadingProgressModule,
        LikeModule,
        AwsS3Module,
        MulterModule.register({
            limits: { fileSize: 1000000 },
            dest: "./uploads"
        })
    ],
    controllers: [],
    providers: []
})
export class AppModule { }
