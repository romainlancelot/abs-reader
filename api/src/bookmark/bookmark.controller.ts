import { Controller, Body, Put, Req, Res, Param, HttpStatus, Get } from "@nestjs/common";
import { BookmarkService } from "./bookmark.service";
import { UpsertBookmarkDto } from "./dto/upsert-bookmark.dto";
import { Response } from "express";
import { Bookmark } from "@prisma/client";
import { CustomisedExpressRequest } from "src/common/models/customised-express-request";

@Controller("bookmarks")
export class BookmarkController {
    public constructor(
        private readonly bookmarkService: BookmarkService
    ) { }

    @Get("books/:bookId")
    public async findUnique(
        @Req() request: CustomisedExpressRequest,
        @Param("bookId") bookId: string,
        @Res() response: Response
    ): Promise<Response> {
        const bookmark: Bookmark = await this.bookmarkService.findUnique(
            request.user.id,
            bookId
        );
        if (!bookmark) {
            return response
                .status(HttpStatus.NOT_FOUND)
                .json();
        }
        return response
            .status(HttpStatus.OK)
            .json(bookmark);
    }

    @Put("books/:bookId")
    public async create(
        @Req() request: CustomisedExpressRequest,
        @Param("bookId") bookId: string,
        @Body() upsertBookmarkDto: UpsertBookmarkDto,
        @Res() response: Response
    ): Promise<Response> {
        const { bookmark, isNew }: { bookmark: Bookmark, isNew: boolean } =
            await this.bookmarkService.upsert(
                request.user.id,
                bookId,
                upsertBookmarkDto
            );
        const status: HttpStatus = isNew ? HttpStatus.CREATED : HttpStatus.OK;
        return response
            .status(status)
            .json(bookmark);
    }
}
