import { Injectable } from "@nestjs/common";
import { UpsertBookmarkDto } from "./dto/upsert-bookmark.dto";
import { PrismaService } from "src/prisma/prisma.service";
import { Bookmark } from "@prisma/client";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";

@Injectable()
export class BookmarkService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    public async findUnique(
        userId: string,
        bookId: string
    ): Promise<Bookmark> {
        try {
            return await this.prisma.bookmark.findUnique({
                where: {
                    userId_bookId: {
                        userId,
                        bookId
                    }
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async upsert(
        userId: string,
        bookId: string,
        upsertBookmarkDto: UpsertBookmarkDto
    ): Promise<{ bookmark: Bookmark; isNew: boolean }> {
        try {
            const existingBookmark = await this.prisma.bookmark.findUnique({
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

            return { bookmark, isNew: !existingBookmark };
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }

    public async delete(
        userId: string,
        bookId: string
    ): Promise<void> {
        try {
            await this.prisma.bookmark.delete({
                where: {
                    userId_bookId: {
                        userId,
                        bookId
                    }
                }
            });
        } catch (error: unknown) {
            throw this.errorHandlerService.handleError(error);
        }
    }
}
