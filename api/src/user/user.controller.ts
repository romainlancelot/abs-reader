import { Body, Controller, Delete, Get, Patch, Res, UseGuards } from "@nestjs/common";
import { JwtGuard } from "src/auth/guard/jwt.guard";
import { UserService } from "./user.service";
import { User } from "@prisma/client";
import { Response } from "express";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";
import { UpdateUserDto } from "./dto/update-user.dto";
import { CustomisedExpressRequest } from "src/common/models/customised-express-request";

@Controller("users")
export class UserController {
    public constructor(
        private readonly userService: UserService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    @UseGuards(JwtGuard)
    @Get("me")
    public async readMe(
        @Res() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const user: Omit<User, "password"> = await this.userService.getUser(
                request.user.id
            );
            return response.status(200).json(user);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard)
    @Patch("me")
    public async updateMe(
        @Res() request: CustomisedExpressRequest,
        @Body() dto: UpdateUserDto,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const user: Omit<User, "password"> = await this.userService.updateUser(
                request.user.id,
                dto
            );
            return response.status(200).json(user);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    @UseGuards(JwtGuard)
    @Delete("me")
    public async deleteMe(
        @Res() request: CustomisedExpressRequest,
        @Res() response: Response
    ): Promise<Response> {
        try {
            const user: Omit<User, "password"> = await this.userService.deleteUser(
                request.user.id
            );
            return response.status(200).json(user);
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
