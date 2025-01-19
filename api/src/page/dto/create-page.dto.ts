import { IsNotEmpty, IsNumber, IsString, IsUUID } from "class-validator";

export class CreatePageDto {
    @IsNotEmpty()
    @IsUUID()
    bookId: string;

    @IsNotEmpty()
    @IsNumber()
    order: number;

    @IsNotEmpty()
    @IsString()
    fileId: string;
}
