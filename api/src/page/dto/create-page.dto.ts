import { IsNotEmpty, IsNumber, IsString, IsUUID } from "class-validator";

export class CreatePageDto {
    @IsUUID()
    @IsNotEmpty()
    bookId: string;

    @IsNotEmpty()
    @IsNumber()
    order: number;

    @IsString()
    @IsNotEmpty()
    fileId: string;

    @IsString()
    @IsNotEmpty()
    fileUrl: string;
}
