import { Injectable, NotFoundException, UnauthorizedException } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { hash, compare } from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { User } from '@prisma/client';
import { ulid } from 'ulid';
import { SignUpAuthDto } from './dto/sign-up-auth.dto';
import { LogInAuthDto } from './dto/log-in-auth.dto';

type UserCreateData = Pick<User, 'email' | 'password' | 'name' | 'tag'>;

@Injectable()
export class AuthService {
    constructor(
        private prisma: PrismaService,
        private jwtService: JwtService
    ) { }

    public async signUp(
        dto: SignUpAuthDto
    ): Promise<Partial<User>> {
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

        if (!user) throw new NotFoundException('User creation failed');

        return user;
    }

    public async logIn(dto: LogInAuthDto): Promise<string> {
        const user: User = await this.prisma.user.findUnique({ where: { email: dto.email } });

        if (
            !user
            || !await compare(dto.password, user.password)
        )
            throw new UnauthorizedException();

        return await this.jwtService.signAsync({
            sub: user.id
        });

    }
}
