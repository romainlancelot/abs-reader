import { Injectable } from "@nestjs/common";
import { CreateBookDto } from "./dto/create-book.dto";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { PrismaService } from "src/prisma/prisma.service";
import { Book, Page, User } from "@prisma/client";
import { UpdateBookDto } from "./dto/update-book.dto";
import { AwsS3Service } from "src/aws-s3/aws-s3.service";
import { CreatePageDto } from "./dto/create-page.dto";

@Injectable()
export class BookService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly errorHandlerService: ErrorHandlerService,
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
            if (!user) {
                return null;
            }

            const { fileId, fileUrl } = await this.awsS3Service.upload(bookCoverFile);
            if (!fileId || !fileUrl) {
                return null;
            }

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
            if (!book) {
                return null;
            }

            return book;
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
                },
                include: {
                    pages: {
                        orderBy: {
                            order: "asc"
                        }
                    }
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async findAll(): Promise<Book[]> {
        try {
            return await this.prisma.book.findMany({
                orderBy: {
                    createdAt: "desc"
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
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
            if (!bookToUpdate) {
                return null;
            }
            if (userId !== bookToUpdate.authorId) {
                return null;
            }

            return await this.prisma.book.update({
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
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
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
            if (!bookToUpdate) {
                return null;
            }

            if (userId !== bookToUpdate.authorId) {
                return null;
            }

            const { fileId, fileUrl }: { fileId: string, fileUrl: string } = await this.awsS3Service.upload(coverFile);
            if (!fileId || !fileUrl) {
                return null;
            }

            const updatedBook: Book = await this.prisma.book.update({
                where: {
                    id: bookToUpdate.id
                },
                data: {
                    coverId: fileId,
                    coverUrl: fileUrl
                }
            });
            if (!updatedBook) {
                return null;
            }
            await this.awsS3Service.delete(bookToUpdate.coverId);

            return updatedBook;
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
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
            if (!bookToUpdate) {
                return null;
            }

            if (userId !== bookToUpdate.authorId) {
                return null;
            }

            const pagesToDelete: Page[] = await this.prisma.page.findMany({
                where: {
                    bookId
                }
            });

            for (const [index, newPageFile] of newPagesFiles.entries()) {
                const { fileId, fileUrl }: { fileId: string, fileUrl: string } = await this.awsS3Service.upload(newPageFile);
                if (!fileId || !fileUrl) {
                    return null;
                }
                const data: CreatePageDto = {
                    bookId,
                    order: index + 1,
                    fileId,
                    fileUrl
                };
                const page: Page = await this.prisma.page.create({ data });
                if (!page) {
                    return null;
                }
            }

            for (const pageToDelete of pagesToDelete) {
                await this.prisma.page.delete({
                    where: {
                        id: pageToDelete.id
                    }
                });
                await this.awsS3Service.delete(pageToDelete.fileId);
            }

            return await this.prisma.book.findUnique({
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
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
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
            if (!bookToDelete) {
                return null;
            }

            if (userId !== bookToDelete.authorId) {
                return null;
            }

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

            for (const fileToDelete of filesToDelete) {
                await this.awsS3Service.delete(fileToDelete);
            }
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }
}
