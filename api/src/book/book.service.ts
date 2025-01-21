import { BadRequestException, ForbiddenException, Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { CreateBookDto } from "./dto/create-book.dto";
import { PrismaService } from "src/prisma/prisma.service";
import { Book, Page, User } from "@prisma/client";
import { UpdateBookDto } from "./dto/update-book.dto";
import { AwsS3Service } from "src/aws-s3/aws-s3.service";
import { CreatePageDto } from "./dto/create-page.dto";

@Injectable()
export class BookService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly awsS3Service: AwsS3Service
    ) { }

    public async create(
        userId: string,
        dto: CreateBookDto,
        bookCoverFile: Express.Multer.File
    ): Promise<Book> {
        try {
            const user: User = await this.prisma.user.findUnique({
                where: {
                    id: userId
                }
            });

            if (!user)
                throw new NotFoundException("The ID of the JWT is unknown.");

            const { fileId, fileUrl } = await this.awsS3Service.upload(bookCoverFile);

            if (!fileId || !fileUrl)
                throw new InternalServerErrorException("File creation failed.");

            const data = {
                title: dto.title,
                authorId: user.id,
                coverId: fileId,
                coverUrl: fileUrl
            };
            const book: Book = await this.prisma.book.create({
                data,
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        }
                    }
                }
            });
            if (!book)
                throw new InternalServerErrorException("Book creation failed.");

            return book;
        } catch (error: unknown) {
            throw error;
        }
    }

    public async findUnique(
        bookId: string
    ): Promise<Book> {
        try {
            const book: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                },
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        }
                    }
                }
            });

            if (!book)
                throw new NotFoundException("Book not found.");

            return book;
        } catch (error: unknown) {
            throw error;
        }
    }

    public async findAll(): Promise<Book[]> {
        try {
            const books: Book[] = await this.prisma.book.findMany({
                orderBy: {
                    createdAt: "desc"
                }
            });

            if (!books)
                throw new InternalServerErrorException();

            return books;
        } catch (error: unknown) {
            throw error;
        }
    }

    public async findAllOf(
        userId: string
    ): Promise<Book[]> {
        try {
            return await this.prisma.book.findMany({
                where: {
                    authorId: userId
                },
                orderBy: {
                    createdAt: "desc"
                }
            });
        } catch (error: unknown) {
            throw error;
        }
    }

    public async updateInformation(
        userId: string,
        bookId: string,
        dto: UpdateBookDto
    ): Promise<Book> {
        try {
            const bookToUpdate: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });

            if (!bookToUpdate)
                throw new BadRequestException("The book does not exist.");

            if (userId !== bookToUpdate.authorId)
                throw new ForbiddenException("The user updating the book is not the owner.");

            const updatedBook: Book = await this.prisma.book.update({
                where: {
                    id: bookId
                },
                data: dto,
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        }
                    }
                }
            });

            if (!updatedBook)
                throw new InternalServerErrorException("Book update failed.");

            return updatedBook;
        } catch (error: unknown) {
            throw error;
        }
    }

    public async updateCover(
        userId: string,
        bookId: string,
        coverFile: Express.Multer.File
    ): Promise<Book> {
        try {
            const bookToUpdate: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });

            if (!bookToUpdate)
                throw new BadRequestException("The book does not exist.");

            if (userId !== bookToUpdate.authorId)
                throw new ForbiddenException("The user updating the book is not the owner.");


            const { fileId, fileUrl }: { fileId: string, fileUrl: string } = await this.awsS3Service.upload(coverFile);

            if (!fileId || !fileUrl)
                throw new InternalServerErrorException("File creation failed.");

            const updatedBook: Book = await this.prisma.book.update({
                where: {
                    id: bookToUpdate.id
                },
                data: {
                    coverId: fileId,
                    coverUrl: fileUrl
                }
            });

            if (!updatedBook)
                throw new InternalServerErrorException("Book update failed.");

            await this.awsS3Service.delete(bookToUpdate.coverId);

            return updatedBook;
        } catch (error: unknown) {
            throw error;
        }
    }

    public async updateContent(
        userId: string,
        bookId: string,
        newPagesFiles: Express.Multer.File[]
    ): Promise<Book> {
        try {
            const bookToUpdate: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });

            if (!bookToUpdate)
                throw new BadRequestException("The book does not exist.");

            if (userId !== bookToUpdate.authorId)
                throw new ForbiddenException("The user updating the book is not the owner.");

            const pagesToDelete: Page[] = await this.prisma.page.findMany({
                where: {
                    bookId
                }
            });

            for (const [index, newPageFile] of newPagesFiles.entries()) {
                const { fileId, fileUrl }: { fileId: string, fileUrl: string } = await this.awsS3Service.upload(newPageFile);

                if (!fileId || !fileUrl)
                    throw new InternalServerErrorException("File creation failed.");

                const data: CreatePageDto = {
                    bookId,
                    order: index + 1,
                    fileId,
                    fileUrl
                };
                const page: Page = await this.prisma.page.create({ data });

                if (!page)
                    throw new InternalServerErrorException("Page creation failed.");
            }

            for (const pageToDelete of pagesToDelete) {
                await this.prisma.page.delete({
                    where: {
                        id: pageToDelete.id
                    }
                });
                await this.awsS3Service.delete(pageToDelete.fileId);
            }

            const book: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                },
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        }
                    }
                }
            });

            if (!book)
                throw new NotFoundException("Updated book not found.");

            return book;
        } catch (error: unknown) {
            throw error;
        }
    }

    public async delete(
        userId: string,
        bookId: string
    ): Promise<void> {
        try {
            const bookToDelete: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                }
            });

            if (!bookToDelete)
                throw new NotFoundException("The book to delete is not found.");

            if (userId !== bookToDelete.authorId)
                throw new ForbiddenException("The user updating the book is not the owner.");

            const pagesToDelete: Page[] = await this.prisma.page.findMany({
                where: {
                    bookId
                }
            });

            const filesToDelete: string[] = pagesToDelete.map((element: Page) => element.fileId);
            filesToDelete.push(bookToDelete.coverId);

            await this.prisma.book.delete({
                where: {
                    id: bookId
                }
            });

            for (const fileToDelete of filesToDelete)
                await this.awsS3Service.delete(fileToDelete);
        } catch (error: unknown) {
            throw error;
        }
    }
}
