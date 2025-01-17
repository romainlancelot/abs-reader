import { Body, Controller, HttpStatus, Post, Res } from "@nestjs/common";
import { AuthService } from "./auth.service";
import { Response } from "express";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { ApiTags } from "@nestjs/swagger";
import { User } from "@prisma/client";
import { SignUpAuthDto } from "./dto/sign-up-auth.dto";
import { LogInAuthDto } from "./dto/log-in-auth.dto";

@Controller("auth")
@ApiTags("Auth")
export class AuthController {
    public constructor(
        private readonly authService: AuthService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    @Post("signup")
    public async signUp(
        @Body() dto: SignUpAuthDto,
        @Res() response: Response
    ): Promise<Response> {
        try {
            await this.authService.signUp(dto);
            return response
                .status(HttpStatus.CREATED)
                .json();
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @Post("login")
    public async logIn(
        @Body() dto: LogInAuthDto,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const jwt: string = await this.authService.logIn(dto);
            return response
                .status(HttpStatus.OK)
                .json({ jwt });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
