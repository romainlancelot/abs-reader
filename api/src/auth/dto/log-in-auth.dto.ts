import { IsEmail, IsNotEmpty, IsString } from "class-validator";

export class LogInAuthDto {
    @IsEmail()
    @IsNotEmpty()
    email: string;

    @IsString()
    @IsNotEmpty()
    password: string;
}
