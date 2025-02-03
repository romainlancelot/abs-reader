import { Bookmark } from "@prisma/client";

export class UpsertBookmarResponsekDto {
    bookmark: Bookmark;
    pageOrder: number;
}
