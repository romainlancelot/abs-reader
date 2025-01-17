import { Module } from "@nestjs/common";
import { BookService } from "./book.service";
import { BookController } from "./book.controller";
import { AwsS3Module } from "src/aws-s3/aws-s3.module";

@Module({
    controllers: [BookController],
    providers: [BookService],
    exports: [BookService],
    imports: [AwsS3Module]
})
export class BookModule { }
