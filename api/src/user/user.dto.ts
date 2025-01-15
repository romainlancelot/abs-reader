import { IsString, IsEmail, IsNotEmpty } from 'class-validator';

export class UpdateUserDto {
    @IsEmail()
    @IsNotEmpty()
    email?: string;

    @IsString()
    @IsNotEmpty()
    password?: string;

    @IsString()
    @IsNotEmpty()
    name?: string;

    @IsString()
    tag?: string;
}
