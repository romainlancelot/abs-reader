import { HttpStatus, Injectable } from '@nestjs/common';
import { PrismaClientKnownRequestError } from '@prisma/client/runtime/library';

@Injectable()
export class PrismaErrorHandlerService {

    public handlePrismaError(
        error: PrismaClientKnownRequestError
    ): {
        statusCode: HttpStatus;
        message: string;
    } {
        let statusCode: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        let message: string = 'An unexpected error occurred';

        switch (error.code) {
            case 'P2002':
                statusCode = HttpStatus.CONFLICT;
                message = 'A unique constraint violation occurred';
                break;
            case 'P2025':
                statusCode = HttpStatus.NOT_FOUND;
                message = 'The requested resource does not exist';
                break;
            default:
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
                message = 'An internal server error occurred';
        }

        return { statusCode, message };
    }

}
