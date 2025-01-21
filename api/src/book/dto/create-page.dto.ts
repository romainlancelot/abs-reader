import { IsNotEmpty, IsNumber, IsString } from "class-validator";

export class CreatePageDto {
    @IsString()
    @IsNotEmpty()
    bookId: string;

    @IsNumber()
    @IsNotEmpty()
    order: number;

    @IsString()
    @IsNotEmpty()
    fileId: string;

    @IsString()
    @IsNotEmpty()
    fileUrl: string;
}
