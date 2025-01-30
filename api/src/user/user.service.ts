import { Injectable, InternalServerErrorException } from "@nestjs/common";
import { PrismaService } from "src/prisma/prisma.service";
import { User } from "@prisma/client";
import { hash } from "bcrypt";
import { UpdateUserDto } from "./dto/update-user.dto";

@Injectable()
export class UserService {
    public constructor(
        private readonly prisma: PrismaService
    ) { }

    public async getUser(userId: string) {
        try {
            const user = await this.prisma.user.findUnique({
                where: { id: userId }
            });
            delete user.password;
            return user;
        } catch (error: any) {
            throw error;
        }
    }

    public async updateUser(
        userId: string,
        dto: UpdateUserDto
    ): Promise<User> {
        try {
            if (dto.password)
                dto.password = await hash(dto.password, 10);

            const user: User = await this.prisma.user.update({
                where: { id: userId },
                data: { ...dto }
            });

            if (!user)
                throw new InternalServerErrorException("User update failed.");

            delete user.password;

            return user;
        } catch (error: any) {
            throw error;
        }
    }

    public async deleteUser(
        userId: string
    ): Promise<User> {
        try {
            const deletedUser: User = await this.prisma.user.delete({
                where: {
                    id: userId
                }
            });

            if (!deletedUser)
                throw new InternalServerErrorException("User deletion failed.");

            return deletedUser;
        } catch (error: any) {
            throw error;
        }
    }
}
