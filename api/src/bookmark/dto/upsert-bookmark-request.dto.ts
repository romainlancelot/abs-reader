import { IsString, IsNotEmpty } from "class-validator";

export class UpsertBookmarkRequestDto {
    @IsString()
    @IsNotEmpty()
    currentPageId: string;
}
