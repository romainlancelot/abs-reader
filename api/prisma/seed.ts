import { Prisma, PrismaClient } from "@prisma/client";
import { hash } from "bcrypt";
import { v4 } from "uuid";

const prisma = new PrismaClient();

const users: Prisma.UserCreateManyInput[] = [];
const usersCount: number = 5;

async function createUsers(): Promise<void> {
    for (let i = 0; i < usersCount; i++) {
        users.push({
            id: v4(),
            email: `user${i}@email.net`,
            password: await hash("password", 10),
            name: `name${i}`,
            tag: `name${i}${v4()}`
        });
    }
}

async function main() {
    await createUsers();
    await prisma.user.createMany({ data: users });
}

main()
    .then(
        async () => {
            await prisma.$disconnect();
        }
    )
    .catch(
        async (e) => {
            console.error(e);
            await prisma.$disconnect();
            process.exit(1);
        }
    );
