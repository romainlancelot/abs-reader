import { Bookmark } from "@prisma/client";

export class FindUniqueBookmarkResponseDto {
    bookmark: Bookmark;
    pageOrder: number;
}
