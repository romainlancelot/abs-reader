import { Injectable } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { UpdateUserDto } from './user.dto';
import { User } from '@prisma/client';
import { hash } from 'bcrypt';
import { ErrorHandlerService } from 'src/common/utils/error-handler/error-handler.service';

@Injectable()
export class UserService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    public async getUser(userId: string) {
        const user = await this.prisma.user.findUnique({
            where: { id: userId }
        });
        delete user.password;
        return user;
    }

    public async updateUser(
        userId: string,
        dto: UpdateUserDto
    ): Promise<User> {
        if (dto.password) {
            dto.password = await hash(dto.password, 10);
        }

        const data = {
            ...dto
        };
        let user: User = null;

        try {
            user = await this.prisma.user.update({
                where: { id: userId },
                data
            });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
        delete user.password;
        return user;
    }

    public async deleteUser(userId: string): Promise<User> {
        try {
            return this.prisma.user.delete({ where: { id: userId } });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
