import { Injectable, InternalServerErrorException, NotFoundException } from "@nestjs/common";
import { UpsertBookmarkDto } from "./dto/upsert-bookmark.dto";
import { PrismaService } from "src/prisma/prisma.service";
import { Bookmark } from "@prisma/client";

@Injectable()
export class BookmarkService {
    public constructor(
        private readonly prisma: PrismaService
    ) { }

    public async findUnique(
        userId: string,
        bookId: string
    ): Promise<Bookmark> {
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

            return bookmark;
        } catch (error: any) {
            throw error;
        }
    }

    public async upsert(
        userId: string,
        bookId: string,
        upsertBookmarkDto: UpsertBookmarkDto
    ): Promise<{ bookmark: Bookmark; isNew: boolean }> {
        try {
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
                    currentPageId: upsertBookmarkDto.currentPageId
                },
                create: {
                    userId,
                    bookId,
                    currentPageId: upsertBookmarkDto.currentPageId
                }
            });

            if (!bookmark)
                throw new InternalServerErrorException("Bookmark upsert failed.");

            return { bookmark, isNew: !existingBookmark };
        } catch (error: any) {
            throw error;
        }
    }

    public async delete(
        userId: string,
        bookId: string
    ): Promise<Bookmark> {
        try {
            const deletedBookmark: Bookmark = await this.prisma.bookmark.delete({
                where: {
                    userId_bookId: {
                        userId,
                        bookId
                    }
                }
            });

            if (!deletedBookmark)
                throw new InternalServerErrorException("Bookmark deletion failed.");

            return deletedBookmark;
        } catch (error: any) {
            throw error;
        }
    }
}
