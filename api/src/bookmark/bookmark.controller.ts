import { Controller, Body, Put, Req, Res, Param, HttpStatus, Get, UseGuards } from "@nestjs/common";
import { BookmarkService } from "./bookmark.service";
import { UpsertBookmarkRequestDto } from "./dto/upsert-bookmark-request.dto";
import { Response } from "express";
import { Bookmark } from "@prisma/client";
import { CustomisedExpressRequest } from "src/common/models/customised-express-request";
import { JwtGuard } from "src/auth/guard/jwt.guard";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";

@Controller("bookmarks")
export class BookmarkController {

    public constructor(
        private readonly bookmarkService: BookmarkService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    @UseGuards(JwtGuard)
    @Put("books/:bookId")
    public async upsert(
        @Req() request: CustomisedExpressRequest,
        @Param("bookId") bookId: string,
        @Body() upsertBookmarkRequestDto: UpsertBookmarkRequestDto,
        @Res() response: Response
    ): Promise<Response> {
        const { bookmark, pageOrder, isNew }: { bookmark: Bookmark, pageOrder: number, isNew: boolean } =
            await this.bookmarkService.upsert(
                request.user.id,
                bookId,
                upsertBookmarkRequestDto
            );

        const status: HttpStatus = isNew ? HttpStatus.CREATED : HttpStatus.OK;
        return response.status(status).json({ bookmark, pageOrder });
    }

    @UseGuards(JwtGuard)
    @Get("books/:bookId")
    public async findUnique(
        @Req() request: CustomisedExpressRequest,
        @Param("bookId") bookId: string,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const { bookmark, pageOrder }: { bookmark: Bookmark, pageOrder: number }
                = await this.bookmarkService.findUnique(
                    request.user.id,
                    bookId
                );

            return response.status(HttpStatus.OK).json({ bookmark, pageOrder });
        } catch (error: any) {
            return this.errorHandlerService.getErrorForControllerLayer(error, response);
        }
    }
}
