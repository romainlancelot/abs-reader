import { BadRequestException, ForbiddenException, Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { CreateBookDto } from "./dto/create-book.dto";
import { PrismaService } from "src/prisma/prisma.service";
import { Book, Page, User, Bookmark } from "@prisma/client";
import { UpdateBookDto } from "./dto/update-book.dto";
import { AwsS3Service } from "src/aws-s3/aws-s3.service";
import { CreatePageDto } from "./dto/create-page.dto";
import { CommonUtilsFileService } from "src/common/utils/common-utils-file/common-utils-file.service";

@Injectable()
export class BookService {

    public constructor(
        private readonly prisma: PrismaService,
        private readonly awsS3Service: AwsS3Service,
        private readonly commonUtilsFileService: CommonUtilsFileService
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

            const buffersToUpload: Buffer[] = [];

            buffersToUpload.push(...await this.convertToPng(bookCoverFile));

            const { fileId, fileUrl }: { fileId: string, fileUrl: string } = await this.awsS3Service.upload(buffersToUpload[0]);

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
        } catch (error: any) {
            throw error;
        }
    }

    public async findUnique(
        bookId: string,
        userId: string
    ): Promise<{ book: Book, isTheReaderTheAuthor: boolean }> {
        try {
            const book: Book = await this.prisma.book.findUnique({
                where: {
                    id: bookId
                },
                include: {
                    author: {
                        select: {
                            id: true,
                            name: true,
                            tag: true
                        }
                    },
                    pages: {
                        orderBy: {
                            order: "asc"
                        }
                    }
                }
            });

            if (!book)
                throw new NotFoundException("Book not found.");

            const isTheReaderTheAuthor: boolean = book.authorId === userId;

            return { book, isTheReaderTheAuthor };
        } catch (error: any) {
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

            for (const book of books) {
                const user: User = await this.prisma.user.findUnique({
                    where: {
                        id: book.authorId
                    }
                });

                if (!user)
                    throw new NotFoundException("Author not found.");

                book.authorId = user.name;
            }

            return books;
        } catch (error: any) {
            throw error;
        }
    }

    public async findAllOf(
        userId: string
    ): Promise<Book[]> {
        try {
            const books: Book[] = await this.prisma.book.findMany({
                where: {
                    authorId: userId
                },
                orderBy: {
                    createdAt: "desc"
                }
            });

            for (const book of books) {
                const user: User = await this.prisma.user.findUnique({
                    where: {
                        id: book.authorId
                    }
                });

                if (!user)
                    throw new NotFoundException("Author not found.");

                book.authorId = user.name;
            }

            return books;
        } catch (error: any) {
            throw error;
        }
    }

    public async findBookmarkedBooks(
        userId: string
    ): Promise<Book[]> {
        try {
            const bookmarks: Bookmark[] = await this.prisma.bookmark.findMany({
                where: {
                    userId
                }
            });
            const books: Book[] = [];
            for (const bookmark of bookmarks) {
                const book: Book = await this.prisma.book.findUnique({
                    where: {
                        id: bookmark.bookId
                    }
                });

                if (!book)
                    throw new NotFoundException("Book not found.");

                if (book) {
                    const user: User = await this.prisma.user.findUnique({
                        where: {
                            id: book.authorId
                        }
                    });

                    if (!user)
                        throw new NotFoundException("Author not found.");

                    book.authorId = user.name;

                    books.push(book);
                }
            }
            return books;
        } catch (error: any) {
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
        } catch (error: any) {
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

            const buffersToUpload: Buffer[] = [];

            buffersToUpload.push(...await this.convertToPng(coverFile));

            const { fileId, fileUrl }: { fileId: string, fileUrl: string } = await this.awsS3Service.upload(buffersToUpload[0]);

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
        } catch (error: any) {
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

            const buffersToUpload: Buffer[] = [];

            for (const newPageFile of newPagesFiles) {
                buffersToUpload.push(...await this.convertToPng(newPageFile));
            }

            for (const [index, bufferToUpload] of buffersToUpload.entries()) {

                const { fileId, fileUrl }: { fileId: string, fileUrl: string } = await this.awsS3Service.upload(bufferToUpload);

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
        } catch (error: any) {
            throw error;
        }
    }

    private async convertToPng(
        file: Express.Multer.File
    ): Promise<Buffer[]> {
        try {
            if (!await this.commonUtilsFileService.isValidMimeType(file)) {
                throw new BadRequestException("Invalid file type. Only pdf, jpeg, jpg, png, and epub are allowed.");
            }

            if (file.mimetype === "application/pdf") {
                return [await this.commonUtilsFileService.convertPdfFiletoPngBuffer(file)];
            } else if (file.mimetype === "application/epub+zip") {
                return await this.commonUtilsFileService.convertEpubFileToPngBuffers(file);
            } else if (file.mimetype === "image/jpeg") {
                return [await this.commonUtilsFileService.convertJpegFileToPngBuffers(file)];
            } else if (file.mimetype === "image/png") {
                return [file.buffer];
            } else {
                throw new BadRequestException("Unsupported file type for conversion.");
            }
        } catch (error: any) {
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
        } catch (error: any) {
            throw error;
        }
    }
}
