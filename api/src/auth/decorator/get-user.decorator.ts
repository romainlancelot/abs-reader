import { createParamDecorator, ExecutionContext } from "@nestjs/common";

export const GetAuthorization = createParamDecorator(
    function (
        data: string | undefined,
        executionContext: ExecutionContext
    ) {
        const request: Express.Request = executionContext.switchToHttp().getRequest();

        if (data) {
            return request.user[data];
        }
        return request.user;
    }
);
