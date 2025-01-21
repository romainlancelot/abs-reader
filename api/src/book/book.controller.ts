import { Controller, Post, Body, Patch, Param, UseGuards, UseInterceptors, UploadedFile, ParseFilePipe, MaxFileSizeValidator, FileTypeValidator, Res, HttpStatus, UploadedFiles, Delete, Get, Req } from "@nestjs/common";
import { BookService } from "./book.service";
import { CreateBookDto } from "./dto/create-book.dto";
import { FileInterceptor, FilesInterceptor } from "@nestjs/platform-express";
import { JwtGuard } from "src/auth/guard/jwt.guard";
import { Book } from "@prisma/client";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { UpdateBookDto } from "./dto/update-book.dto";
import { Response } from "express";
import { CustomisedExpressRequest } from "src/common/models/customised-express-request";

@Controller("books")
export class BookController {
    public constructor(
        private readonly bookService: BookService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    @UseGuards(JwtGuard)
    @Post()
    @UseInterceptors(FileInterceptor("coverFile"))
    public async create(
        @UploadedFile(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /.(jpeg|jpg|pdf|png)$/ })
                ]
            })
        ) coverFile: Express.Multer.File,
        @Req() request: CustomisedExpressRequest,
        @Body() dto: CreateBookDto,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const book: Book = await this.bookService.create(
                request.user.id,
                dto,
                coverFile
            );
            if (!book) {
                return null;
            }
            return response.status(HttpStatus.CREATED).json(book);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @Get(":bookId")
    public async findUnique(
        @Param("bookId") bookId: string,
        @Res() response: Response
    ): Promise<Response> {
        const book: Book = await this.bookService.findUnique(bookId);

        if (!book) {
            return response.status(HttpStatus.NOT_FOUND).json();
        }

        return response.status(HttpStatus.OK).json(book);
    }

    @Get()
    public async findMany(
        @Res() response: Response
    ): Promise<Response> {
        const books: Book[] = await this.bookService.findAll();
        if (!books)
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
        @Body() dto: UpdateBookDto,
        @Res() response: Response,
        @Req() request: CustomisedExpressRequest
    ): Promise<Response> {
        const book: Book = await this.bookService.updateInformation(
            request.user.id,
            bookId,
            dto
        );
        if (!book) {
            return response
                .status(HttpStatus.NOT_FOUND)
                .json();
        }

        return response
            .status(HttpStatus.OK)
            .json(book);
    }

    @UseGuards(JwtGuard)
    @Patch(":bookId/cover")
    @UseInterceptors(FileInterceptor("coverFile"))
    public async updateCover(
        @UploadedFile(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /.(jpeg|jpg|pdf|png)$/ })
                ]
            })
        ) coverFile: Express.Multer.File,
        @Param("bookId") bookId: string,
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        const book: Book = await this.bookService.updateCover(
            request.user.id,
            bookId,
            coverFile
        );
        if (!book) {
            return response
                .status(HttpStatus.NOT_FOUND)
                .json();
        }

        return response
            .status(HttpStatus.OK)
            .json(book);
    }

    @UseGuards(JwtGuard)
    @Patch(":bookId/content")
    @UseInterceptors(FilesInterceptor("newPagesFiles"))
    public async updateContent(
        @Param("bookId") bookId: string,
        @UploadedFiles(
            new ParseFilePipe({
                validators: [
                    new MaxFileSizeValidator({ maxSize: 1000000 }),
                    new FileTypeValidator({ fileType: /^(application\/pdf|image\/jpeg|image\/jpg|image\/png)$/ })
                ]
            })
        ) newPagesFiles: Express.Multer.File[],
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const updatedBook: Book = await this.bookService.updateContent(
                request.user.id,
                bookId,
                newPagesFiles
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
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            await this.bookService.delete(request.user.id, bookId);
            return response.status(HttpStatus.OK).json();
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
