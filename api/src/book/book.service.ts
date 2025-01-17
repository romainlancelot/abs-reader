import { Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { CreateBookDto } from "./dto/create-book.dto";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { PrismaService } from "src/prisma/prisma.service";
import { Book, Page, User } from "@prisma/client";

@Injectable()
export class BookService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    public async create(
        dto: CreateBookDto,
        bookCoverFileS3Key: string,
        userId
    ): Promise<Book> {
        try {
            const user: User = await this.prisma.user.findUnique({
                where: {
                    id: userId
                }
            });
            if (!user) throw new NotFoundException("User not found.");

            const data = {
                title: dto.title,
                coverS3Key: bookCoverFileS3Key,
                authorId: user.id
            };
            const book: Book = await this.prisma.book.create({ data });
            if (!book) throw new InternalServerErrorException("Resource creation failed.");

            return book;
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async findAll(
        userId: string
    ): Promise<Book[]> {
        try {
            return await this.prisma.book.findMany({
                where: {
                    authorId: userId
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async findUnique(
        bookId: string
    ): Promise<Book> {
        try {
            return await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async updateContent(
        bookId: string,
        uploadedPages: Omit<Page, "id">[]
    ): Promise<void> {
        try {
            await this.prisma.page.deleteMany({
                where: {
                    bookId
                }
            });

            await this.prisma.page.createMany({
                data: uploadedPages
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async delete(
        bookId: string
    ): Promise<void> {
        try {
            await this.prisma.book.delete({
                where: {
                    id: bookId
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }
}
