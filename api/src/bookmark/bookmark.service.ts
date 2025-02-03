import { BadRequestException, Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { UpsertBookmarkRequestDto } from "./dto/upsert-bookmark-request.dto";
import { PrismaService } from "src/prisma/prisma.service";
import { Bookmark, Page } from "@prisma/client";
import { BookWithPages } from "src/book/entities/book.entity";

@Injectable()
export class BookmarkService {

    public constructor(
        private readonly prisma: PrismaService
    ) { }

    public async upsert(
        userId: string,
        bookId: string,
        upsertBookmarkRequestDto: UpsertBookmarkRequestDto
    ): Promise<{ bookmark: Bookmark, pageOrder: number, isNew: boolean }> {
        try {
            const pageToBookmark: Page = await this.prisma.page.findUnique({
                where: {
                    id: upsertBookmarkRequestDto.currentPageId
                }
            });

            if (!pageToBookmark)
                throw new NotFoundException("The page to bookmark is not found.");

            const book: BookWithPages = await this.prisma.book.findUnique({
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
                throw new NotFoundException("The book to bookmark is not found.");

            if (!book.pages.some((page: Page) => page.id === pageToBookmark.id))
                throw new BadRequestException("The page does not belong to the book.");

            const existingBookmark: Bookmark = await this.prisma.bookmark.findUnique({
                where: {
                    userId_bookId: {
                        userId,
                        bookId
                    }
                }
            });

            const bookmark = await this.prisma.bookmark.upsert({
                where: {
                    userId_bookId: {
                        userId,
                        bookId
                    }
                },
                update: {
                    currentPageId: upsertBookmarkRequestDto.currentPageId
                },
                create: {
                    userId,
                    bookId,
                    currentPageId: upsertBookmarkRequestDto.currentPageId
                }
            });

            if (!bookmark)
                throw new InternalServerErrorException("Bookmark upsert failed.");

            return {
                bookmark,
                pageOrder: pageToBookmark.order,
                isNew: !existingBookmark
            };
        } catch (error: any) {
            throw error;
        }
    }

    public async findUnique(
        userId: string,
        bookId: string
    ): Promise<{ bookmark: Bookmark, pageOrder: number }> {
        try {
            const bookmark: Bookmark = await this.prisma.bookmark.findUnique({
                where: {
                    userId_bookId: {
                        userId,
                        bookId
                    }
                }
            });

            if (!bookmark)
                throw new NotFoundException("Bookmark not found.");

            const page: Page = await this.prisma.page.findFirst({
                where: {
                    id: bookmark.currentPageId
                }
            });

            if (!page)
                throw new NotFoundException("Page associated with bookmark not found.");

            return {
                bookmark,
                pageOrder: page.order
            };
        } catch (error: any) {
            throw error;
        }
    }
}
