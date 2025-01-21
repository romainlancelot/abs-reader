import { PrismaClient } from "@prisma/client";
import { hash } from "bcrypt";

const prisma = new PrismaClient();
const users = [
    {
        id: "0dab8e9b-2699-4b3e-8f47-cdcdb30414e6",
        email: "admin@gmail.com",
        password: hash("azrty", 10),
        firstname: "admin firstname",
        lastname: "admin lastname",
        role: "ADMINISTRATOR",
    },
];

async function main() {
    //   await prisma.user.create();
}
main()
    .then(async () => {
        await prisma.$disconnect();
    })
    .catch(async (e) => {
        console.error(e);
        await prisma.$disconnect();
        process.exit(1);
    });
