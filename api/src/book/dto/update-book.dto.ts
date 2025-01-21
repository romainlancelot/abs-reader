import { IsNotEmpty, IsOptional, IsString } from "class-validator";

export class UpdateBookDto {
    @IsString()
    @IsOptional()
    @IsNotEmpty()
    title?: string;
}
