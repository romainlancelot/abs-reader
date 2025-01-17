import { Injectable, NotFoundException, UnauthorizedException } from "@nestjs/common";
import { PrismaService } from "src/prisma/prisma.service";
import { hash, compare } from "bcrypt";
import { JwtService } from "@nestjs/jwt";
import { User } from "@prisma/client";
import { ulid } from "ulid";
import { SignUpAuthDto } from "./dto/sign-up-auth.dto";
import { LogInAuthDto } from "./dto/log-in-auth.dto";
import { ErrorHandlerService } from "src/common/utils/error-handler/error-handler.service";

type UserCreateData = Pick<User, "email" | "password" | "name" | "tag">;

@Injectable()
export class AuthService {
    public constructor(
        private readonly prisma: PrismaService,
        private readonly jwtService: JwtService,
        private readonly errorHandlerService: ErrorHandlerService
    ) { }

    public async signUp(
        dto: SignUpAuthDto
    ): Promise<Partial<User>> {
        try {
            const hashedPassword: string = await hash(dto.password, 10);
            const tag: string = `${dto.name}${ulid()}`;
            const data: UserCreateData = {
                email: dto.email,
                password: hashedPassword,
                name: dto.name,
                tag
            };
            const user: Partial<User> = await this.prisma.user.create({
                data,
                select: {
                    id: true,
                    email: true,
                    name: true
                }
            });

            if (!user) throw new NotFoundException("User creation failed");

            return user;
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }

    public async logIn(
        dto: LogInAuthDto
    ): Promise<string> {
        try {
            const user: User = await this.prisma.user.findUnique({
                where: {
                    email: dto.email
                }
            });
            if (!user || !await compare(dto.password, user.password))
                throw new UnauthorizedException();

            return await this.jwtService.signAsync({
                sub: user.id
            });
        } catch (error: unknown) {
            throw await this.errorHandlerService.handleError(error);
        }
    }
}
