import { IsString, IsNotEmpty } from "class-validator";

export class UpsertBookmarkDto {
    @IsString()
    @IsNotEmpty()
    currentPageId: string;
}
