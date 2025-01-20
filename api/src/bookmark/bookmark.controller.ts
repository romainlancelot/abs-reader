import { Controller, Body, Put, Req, Res, Param, HttpStatus } from "@nestjs/common";
import { BookmarkService } from "./bookmark.service";
import { UpsertBookmarkDto } from "./dto/upsert-bookmark.dto";
import { Response } from "express";
import { Bookmark } from "@prisma/client";

@Controller("bookmarks")
export class BookmarkController {
    public constructor(
        private readonly bookmarkService: BookmarkService
    ) { }

    @Put("books/:bookId")
    public async create(
        @Req() request,
        @Param("bookId") bookId: string,
        @Body() upsertBookmarkDto: UpsertBookmarkDto,
        @Res() response: Response
    ): Promise<Response> {
        const { bookmark, isNew }: {
            bookmark: Bookmark,
            isNew: boolean
        } = await this.bookmarkService.upsert(
            request.user.id,
            bookId,
            upsertBookmarkDto
        );
        const status: HttpStatus = isNew ? HttpStatus.CREATED : HttpStatus.OK;
        return response.status(status).json(bookmark);
    }
}
