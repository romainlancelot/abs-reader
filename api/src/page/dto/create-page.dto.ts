import { IsNotEmpty, IsNumber, IsUUID } from 'class-validator';

export class CreatePageDto {
    @IsNotEmpty()
    @IsUUID()
    bookId: string;

    @IsNotEmpty()
    @IsNumber()
    order: number;
}
