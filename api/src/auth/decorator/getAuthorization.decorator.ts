import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { Role } from '@prisma/client';
import { decode } from 'jsonwebtoken';

export type DecodedToken = {
    sub: string;
    role: Role;
    iat: number;
    exp: number;
} | null;

export const GetAuthorization = createParamDecorator(
    (data: unknown, ctx: ExecutionContext): DecodedToken => {
        const request = ctx.switchToHttp().getRequest();
        const authorization = request.headers['authorization'];
        if (!authorization) {
            return null;
        }
        const token = authorization.split(' ')[1];
        try {
            const decoded = decode(token);
            return decoded as DecodedToken;
        } catch (error) {
            return null;
        }
    }
);
