import { ForbiddenException, Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { CreateBookDto } from "./dto/create-book.dto";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { PrismaService } from "src/prisma/prisma.service";
import { Book, File, Page, User } from "@prisma/client";
import { UpdateBookDto } from "./dto/update-book.dto";
import { FileService } from "src/file/file.service";
import { BookInfoAndData } from "./entities/book.entity";

@Injectable()
export class BookService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly errorHandlerService: ErrorHandlerService,
        private readonly fileService: FileService
    ) { }

    public async create(
        userId: string,
        dto: CreateBookDto,
        bookCoverFile: Express.Multer.File
    ): Promise<Book> {
        try {
            const file: File = await this.fileService.create(bookCoverFile);
            if (!file) {
                throw new InternalServerErrorException("Cover file creation failed.");
            }

            const user: User = await this.prisma.user.findUnique({
                where: {
                    id: userId
                }
            });
            if (!user) throw new NotFoundException("User not found.");

            const data = {
                title: dto.title,
                coverId: file.name,
                authorId: user.id
            };
            const book: Book = await this.prisma.book.create({
                data,
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        },
                        include: {
                            file: true
                        }
                    }
                }
            });
            if (!book) throw new InternalServerErrorException("Book creation failed.");

            return book;
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async findUnique(
        bookId: string
    ): Promise<BookInfoAndData> {
        try {
            const book: BookInfoAndData = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                },
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        },
                        include: {
                            file: true
                        }
                    }
                }
            });
            if (!book) throw new NotFoundException("Book not found.");

            return book;
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async findAll(): Promise<Book[]> {
        try {
            return await this.prisma.book.findMany();
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async updateInformation(
        bookId: string,
        dto?: UpdateBookDto,
        coverFile?: Express.Multer.File
    ): Promise<Book> {
        try {
            const bookToUpdate: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });
            if (!bookToUpdate) {
                throw new NotFoundException("Book not found.");
            }

            if (dto.title) {
                await this.prisma.book.update({
                    where: {
                        id: bookId
                    },
                    data: {
                        title: dto.title
                    }
                });
            }
            if (coverFile) {
                await this.updateCover(bookId, coverFile);
            }

            return await this.prisma.book.findUnique({
                where: {
                    id: bookId
                },
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        },
                        include: {
                            file: true
                        }
                    }
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    private async updateCover(
        bookId: string,
        coverFile: Express.Multer.File
    ): Promise<Book> {
        const bookToUpdate: Book = await this.prisma.book.findUnique({
            where: {
                id: bookId
            }
        });
        await this.fileService.deleteFile(bookToUpdate.coverId);

        const file: File = await this.fileService.create(coverFile);
        if (!file) {
            throw new InternalServerErrorException("File creation failed.");
        }

        const updatedBook: Book = await this.prisma.book.update({
            where: {
                id: bookToUpdate.id
            },
            data: {
                coverId: file.name
            }
        });
        if (!updatedBook) {
            throw new InternalServerErrorException("Book update failed.");
        }

        return updatedBook;
    }

    public async updateContent(
        userId: string,
        bookId: string,
        newPages: Express.Multer.File[]
    ): Promise<Book> {
        try {
            const bookToUpdate: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });
            if (!bookToUpdate) {
                throw new NotFoundException("Book not found.");
            }

            if (userId !== bookToUpdate.authorId) {
                throw new ForbiddenException("The user who is updating the book is not the owner.");
            }

            const pagesToDelete: Page[] = await this.prisma.page.findMany({
                where: {
                    bookId
                }
            });

            for (const pageToDelete of pagesToDelete) {
                await this.fileService.deleteFile(pageToDelete.fileId);
            }

            await this.prisma.page.deleteMany({
                where: {
                    bookId
                }
            });

            for (const [index, newPage] of newPages.entries()) {
                const file: File = await this.fileService.create(newPage);
                await this.prisma.page.create({
                    data: {
                        bookId,
                        order: index + 1,
                        fileId: file.name
                    }
                });
            }

            return await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });

        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async delete(
        userId: string,
        bookId: string
    ): Promise<void> {
        try {
            const bookToUpdate: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });
            if (!bookToUpdate) {
                throw new NotFoundException("Book not found.");
            }

            if (userId !== bookToUpdate.authorId) {
                throw new ForbiddenException("The user who is updating the book is not the owner.");
            }

            const pagesToDelete: Page[] = await this.prisma.page.findMany({
                where: {
                    bookId
                }
            });

            for (const pageToDelete of pagesToDelete) {
                await this.fileService.deleteFile(pageToDelete.fileId);
            }

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
