import { HttpException, HttpStatus, Injectable } from "@nestjs/common";
import { PrismaErrorHandlerService } from "../prisma-error-handler/prisma-error-handler.service";
import { PrismaClientKnownRequestError } from "@prisma/client/runtime/library";

@Injectable()
export class ErrorHandlerService {
    constructor(
        private readonly prismaErrorHandlerService: PrismaErrorHandlerService
    ) { }

    public async handleError(
        error: unknown
    ): Promise<never> {
        if (error instanceof HttpException) {
            console.log(error);
            throw error;
        } else if (error instanceof PrismaClientKnownRequestError) {
            const errorHandled: { statusCode: number; message: string } = this.prismaErrorHandlerService.handlePrismaError(error);
            throw new HttpException(errorHandled.message, errorHandled.statusCode);
        } else {
            throw new HttpException(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
