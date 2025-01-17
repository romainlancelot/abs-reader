import { ExtractJwt, Strategy } from "passport-jwt";
import { PassportStrategy } from "@nestjs/passport";
import { Injectable } from "@nestjs/common";
import { ConfigService } from "@nestjs/config";
import { PrismaService } from "src/prisma/prisma.service";
import { User } from "@prisma/client";

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy, "jwt") {
    public constructor(
        public configService: ConfigService,
        private prisma: PrismaService
    ) {
        super({
            jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
            ignoreExpiration: false,
            secretOrKey: configService.get("JWT_SECRET")
        });
    }

    public async validate(payload: { sub: string }): Promise<User> {
        const user = await this.prisma.user.findUnique({
            where: {
                id: payload.sub
            }
        });
        delete user.password;
        return user;
    }
}
