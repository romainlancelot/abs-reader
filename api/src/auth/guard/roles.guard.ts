import { Injectable, CanActivate, ExecutionContext } from "@nestjs/common";
import { Reflector } from "@nestjs/core";
import { User } from "@prisma/client";
import { PrismaService } from "src/prisma/prisma.service";

@Injectable()
export class RolesGuard implements CanActivate {
    constructor(
        private readonly reflector: Reflector,
        private readonly prisma: PrismaService
    ) { }

    async canActivate(
        context: ExecutionContext
    ): Promise<boolean> {
        const request = context.switchToHttp().getRequest();
        const userId = request.user?.id;

        if (!userId) return false;

        const user: User = await this.prisma.user.findUnique({
            where: {
                id: userId
            }
        });

        if (user)
            return true;
        else
            return false;
    }
}
