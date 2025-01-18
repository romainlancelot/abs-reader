import { Controller, Post, Body, Patch, Param, UseGuards, UseInterceptors, UploadedFile, ParseFilePipe, MaxFileSizeValidator, FileTypeValidator, Request, Res, HttpStatus, UploadedFiles, Delete, Get } from "@nestjs/common";
import { BookService } from "./book.service";
import { CreateBookDto } from "./dto/create-book.dto";
import { FileInterceptor, FilesInterceptor } from "@nestjs/platform-express";
import { AwsS3Service } from "src/aws-s3/aws-s3.service";
import { JwtGuard } from "src/auth/guard/jwt.guard";
import { Book, Page } from "@prisma/client";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { UpdateBookDto } from "./dto/update-book.dto";
import { response } from "express";

@Controller("books")
export class BookController {
    public constructor(
        private readonly bookService: BookService,
        private readonly awsS3Service: AwsS3Service,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    @UseGuards(JwtGuard)
    @Post()
    @UseInterceptors(FileInterceptor("file"))
    public async create(
        @UploadedFile(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /.(pdf|jpeg|png|jpg)$/ })
                ]
            })
        ) bookCover: Express.Multer.File,
        @Request() request,
        @Body() dto: CreateBookDto,
        @Res() response
    ): Promise<Response> {
        console.log("here3");
        try {
            const { s3Key } = await this.awsS3Service.uploadFile(bookCover);
            const book: Book = await this.bookService.create(
                dto,
                s3Key,
                request.user.id
            );
            if (!book) throw new Error("Resource creation failed");
            return response
                .status(HttpStatus.CREATED)
                .json(book);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @Get("id")
    public async findUnique(
        @Param("id") bookId: string,
        @Res() response
    ): Promise<Response> {
        const book: Book = await this.bookService.findUnique(bookId);
        if (!book)
            return response
                .status(HttpStatus.NOT_FOUND)
                .json();

        return response
            .status(HttpStatus.OK)
            .json(book);
    }

    @UseGuards(JwtGuard)
    @Patch(":id/information")
    public async updateInformation(
        @Param("id") bookId: string,
        @Body() dto: UpdateBookDto
    ) {
        const book: Book = await this.bookService.updateInformation(bookId, dto);
        if (!book)
            return response
                .status(HttpStatus.NOT_FOUND)
                .json();

        return response
            .status(HttpStatus.OK)
            .json(book);
    }

    @UseGuards(JwtGuard)
    @Patch(":id/content")
    @UseInterceptors(FilesInterceptor("files"))
    public async updateContent(
        @Param("id") bookId: string,
        @UploadedFiles(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /\.(pdf|jpeg|png|jpg)$/ })
                ]
            })
        ) files: Express.Multer.File[],
        @Request() request,
        @Res() response
    ): Promise<Response> {
        try {
            const book: Book = await this.bookService.findUnique(bookId);
            if (!book)
                return response.status(HttpStatus.NOT_FOUND).json();

            if (book.authorId !== request.user.id)
                return response.status(HttpStatus.FORBIDDEN).json();

            const uploadedPages: Omit<Page, "id">[] = await Promise.all(
                files.map(async function (file: Express.Multer.File, index: number) {
                    const { s3Key, extensionFile } = await this.awsS3Service.uploadFile(file);
                    return {
                        bookId,
                        order: index + 1,
                        s3Key,
                        extensionFile
                    };
                })
            );

            const updatedBook = await this.bookService.updateContent(bookId, uploadedPages);

            return response
                .status(HttpStatus.OK)
                .json(updatedBook);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard)
    @Delete(":id")
    public async delete(
        @Param("id") bookId: string,
        @Request() request,
        @Res() response
    ): Promise<Response> {
        try {
            await this.bookService.delete(bookId);
            return response
                .status(HttpStatus.OK)
                .json();
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
