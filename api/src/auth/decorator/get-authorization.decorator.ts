import { createParamDecorator, ExecutionContext } from "@nestjs/common";
import { decode } from "jsonwebtoken";

export type DecodedToken = {
    sub: string;
    iat: number;
    exp: number;
} | null;

export const GetAuthorization = createParamDecorator(
    function (
        data: unknown,
        executionContext: ExecutionContext
    ): DecodedToken {
        const request = executionContext.switchToHttp().getRequest();
        const authorization = request.headers["authorization"];
        if (!authorization) return null;

        const token = authorization.split(" ")[1];
        try {
            const decoded = decode(token);
            return decoded as DecodedToken;
        } catch (error) {
            return null;
        }
    }
);
