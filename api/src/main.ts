import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";
import { INestApplication, ValidationPipe } from "@nestjs/common";
import { DocumentBuilder, SwaggerModule } from "@nestjs/swagger";

const initSwagger = (app: INestApplication) => {
    const config = new DocumentBuilder().setTitle("Cinema API").build();

    const document = SwaggerModule.createDocument(app, config);

    SwaggerModule.setup("api", app, document);
};

async function bootstrap() {
    const app = await NestFactory.create(AppModule);
    app.enableCors();
    app.useGlobalPipes(new ValidationPipe({ whitelist: true }));
    initSwagger(app);
    await app.listen(3000);
}
bootstrap();
