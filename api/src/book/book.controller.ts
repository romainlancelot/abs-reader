import { Controller, Post, Body, Patch, Param, UseGuards, UseInterceptors, UploadedFile, ParseFilePipe, MaxFileSizeValidator, FileTypeValidator, Res, HttpStatus, UploadedFiles, Delete, Get, Req, Request } from "@nestjs/common";
import { BookService } from "./book.service";
import { CreateBookDto } from "./dto/create-book.dto";
import { FileInterceptor, FilesInterceptor } from "@nestjs/platform-express";
import { AwsS3Service } from "src/aws-s3/aws-s3.service";
import { JwtGuard } from "src/auth/guard/jwt.guard";
import { Book } from "@prisma/client";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { UpdateBookDto } from "./dto/update-book.dto";
import { Response } from "express";

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
                    new FileTypeValidator({ fileType: /.(jpeg|jpg|pdf|png)$/ })
                ]
            })
        ) bookCoverFile: Express.Multer.File,
        @Req() request,
        @Body() dto: CreateBookDto,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const book: Book = await this.bookService.create(
                request.user.id,
                dto,
                bookCoverFile
            );
            if (!book) throw new Error("Book creation failed");
            return response.status(HttpStatus.CREATED).json(book);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @Get(":bookId")
    public async findUnique(
        @Param("bookId") bookId: string,
        @Res() response
    ): Promise<Response> {
        const book: Book = await this.bookService.findUnique(bookId);

        if (!book)
            return response.status(HttpStatus.NOT_FOUND).json();

        return response.status(HttpStatus.OK).json(book);
    }

    @Get()
    public async findMany(
        @Res() response
    ): Promise<Response> {
        const books: Book[] = await this.bookService.findAll();
        if (!books || books.length === 0)
            return response
                .status(HttpStatus.NOT_FOUND)
                .json();

        return response
            .status(HttpStatus.OK)
            .json(books);
    }

    @UseGuards(JwtGuard)
    @Patch(":bookId/information")
    public async updateInformation(
        @Param("bookId") bookId: string,
        @Res() response,
        @Body() dto?: UpdateBookDto,
        @UploadedFile(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /.(jpeg|jpg|pdf|png)$/ })
                ]
            })
        ) coverFile?: Express.Multer.File
    ) {
        const book: Book = await this.bookService.updateInformation(bookId, dto, coverFile);
        if (!book) {
            return response.status(HttpStatus.NOT_FOUND).json();
        }

        return response.status(HttpStatus.OK).json(book);
    }

    @UseGuards(JwtGuard)
    @Patch(":bookId/content")
    @UseInterceptors(FilesInterceptor("files"))
    public async updateContent(
        @Param("bookId") bookId: string,
        @UploadedFiles(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /^(application\/pdf|image\/jpeg|image\/jpg|image\/png)$/ })
                ]
            })
        ) files: Express.Multer.File[],
        @Request() request,
        @Res() response
    ): Promise<Response> {
        try {
            const updatedBook: Book = await this.bookService.updateContent(
                request.user.id,
                bookId,
                files
            );
            return response.status(HttpStatus.OK).json(updatedBook);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard)
    @Delete(":bookId")
    public async delete(
        @Param("bookId") bookId: string,
        @Request() request,
        @Res() response
    ): Promise<Response> {
        try {
            await this.bookService.delete(request.user.id, bookId);
            return response.status(HttpStatus.OK).json();
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
