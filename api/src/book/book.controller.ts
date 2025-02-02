import { Controller, Post, Body, Patch, Param, UseGuards, UseInterceptors, UploadedFile, ParseFilePipe, MaxFileSizeValidator, FileTypeValidator, Res, HttpStatus, UploadedFiles, Delete, Get, Req, BadRequestException } from "@nestjs/common";
import { BookService } from "./book.service";
import { CreateBookDto } from "./dto/create-book.dto";
import { FileInterceptor, FilesInterceptor } from "@nestjs/platform-express";
import { JwtGuard } from "src/auth/guard/jwt.guard";
import { Book } from "@prisma/client";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { UpdateBookDto } from "./dto/update-book.dto";
import { Response } from "express";
import { CustomisedExpressRequest } from "src/common/models/customised-express-request";
import { StringService } from "src/common/utils/string/string.service";

@Controller("books")
export class BookController {

    public constructor(
        private readonly bookService: BookService,
        private readonly errorHandlerService: ErrorHandlerService,
        private readonly stringService: StringService
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
            const createdBook: Book = await this.bookService.create(
                request.user.id,
                dto,
                coverFile
            );

            return response
                .status(HttpStatus.CREATED)
                .json({ createdBook });
        } catch (error: any) {
            return this.errorHandlerService
                .getErrorForControllerLayer(
                    error,
                    response
                );
        }
    }

    @UseGuards(JwtGuard)
    @Get("mine")
    public async findManyOfMine(
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const books: Book[] = await this.bookService.findAllOf(request.user.id);
            return response
                .status(HttpStatus.OK)
                .json({ books });
        } catch (error: any) {
            return this.errorHandlerService
                .getErrorForControllerLayer(
                    error,
                    response
                );
        }
    }

    @UseGuards(JwtGuard)
    @Get("bookmarks")
    public async findBookmarkedBooks(
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const books: Book[] = await this.bookService.findBookmarkedBooks(request.user.id);

            return response
                .status(HttpStatus.OK)
                .json({ books });
        } catch (error: any) {
            return this.errorHandlerService.getErrorForControllerLayer(
                error,
                response
            );
        }
    }

    @UseGuards(JwtGuard)
    @Get(":bookId")
    public async findUnique(
        @Param("bookId") bookId: string,
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            if (!this.stringService.areAllUuids(bookId))
                throw new BadRequestException("Book ID is invalid.");

            const { book, isTheReaderTheAuthor } = await this.bookService.findUnique(bookId, request.user.id);

            return response.status(HttpStatus.OK).json({ book, isTheReaderTheAuthor });
        } catch (error: any) {
            return this.errorHandlerService.getErrorForControllerLayer(error, response);
        }
    }

    @Get()
    public async findMany(
        @Res() response: Response
    ): Promise<Response> {
        try {
            const books: Book[] = await this.bookService.findAll();

            return response
                .status(HttpStatus.OK)
                .json({ books });
        } catch (error: any) {
            return this.errorHandlerService
                .getErrorForControllerLayer(
                    error,
                    response
                );
        }
    }

    @UseGuards(JwtGuard)
    @Patch(":bookId/information")
    public async updateInformation(
        @Param("bookId") bookId: string,
        @Body() dto: UpdateBookDto,
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            if (!this.stringService.areAllUuids(bookId))
                throw new BadRequestException("Book ID is invalid.");

            const book: Book = await this.bookService.updateInformation(
                request.user.id,
                bookId,
                dto
            );

            return response
                .status(HttpStatus.OK)
                .json({ book });
        } catch (error: any) {
            return this.errorHandlerService
                .getErrorForControllerLayer(
                    error,
                    response
                );
        }
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
        try {
            if (!this.stringService.areAllUuids(bookId))
                throw new BadRequestException("Book ID is invalid.");

            const book: Book = await this.bookService.updateCover(
                request.user.id,
                bookId,
                coverFile
            );

            return response
                .status(HttpStatus.OK)
                .json({ book });
        } catch (error: any) {
            return this.errorHandlerService
                .getErrorForControllerLayer(
                    error,
                    response
                );
        }
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
                    new FileTypeValidator({ fileType: /^(application\/pdf|image\/jpeg|image\/jpg|image\/png|application\/epub\+zip)$/ })
                ]
            })
        ) newPagesFiles: Express.Multer.File[],
        @Req() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            if (!this.stringService.areAllUuids(bookId))
                throw new BadRequestException("Book ID is invalid.");

            const updatedBook: Book = await this.bookService.updateContent(
                request.user.id,
                bookId,
                newPagesFiles
            );
            return response
                .status(HttpStatus.OK)
                .json({ updatedBook });
        } catch (error: any) {
            return this.errorHandlerService
                .getErrorForControllerLayer(
                    error,
                    response
                );
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
            if (!this.stringService.areAllUuids(bookId))
                throw new BadRequestException("Book ID is invalid.");

            await this.bookService.delete(request.user.id, bookId);

            return response.status(HttpStatus.OK).json();
        } catch (error: any) {
            return this.errorHandlerService
                .getErrorForControllerLayer(
                    error,
                    response
                );
        }
    }
}
