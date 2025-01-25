import { Injectable } from "@nestjs/common";
import EPub from "epub";
import { pdfToPng, PngPageOutput } from "pdf-to-png-converter";
import * as fs from "fs";
import puppeteer from "puppeteer";
import sharp from "sharp";
import { extname } from "path";

@Injectable()
export class CommonUtilsFileService {
    public getMimeType(
        fileExtension: string
    ): string {
        const mimeTypes: { [key: string]: string } = {
            "jpeg": "image/jpeg",
            "jpg": "image/jpeg",
            "png": "image/png",
            "pdf": "application/pdf",
            "epub": "application/epub+zip"
        };
        return mimeTypes[fileExtension.toLowerCase()] || "application/octet-stream";
    }

    public isValidMimeType(file: Express.Multer.File): boolean {
        const fileExtension = extname(file.originalname).slice(1);
        const expectedMimeType = this.getMimeType(fileExtension);
        const allowedMimeTypes = [
            "image/jpeg",
            "image/png",
            "application/pdf",
            "application/epub+zip"
        ];
        return allowedMimeTypes.includes(file.mimetype) && file.mimetype === expectedMimeType;
    }

    public isZipFile(
        buffer: Buffer
    ): boolean {
        const zipSignature = [0x50, 0x4B, 0x03, 0x04];
        return buffer.slice(0, 4).every((byte, index) => byte === zipSignature[index]);
    }

    public extractExtension(
        filename: string
    ): string {
        const match: RegExpMatchArray = filename.match(/\.(jpeg|jpg|pdf|png|epub+zip)$/i);
        if (match) {
            return match[0].substring(1);
        }
        return null;
    }

    public async convertPdfFiletoPngBuffer(
        file: Express.Multer.File
    ): Promise<Buffer> {
        try {
            const pngPages: PngPageOutput[] = await pdfToPng(
                file.buffer,
                {
                    disableFontFace: false,
                    useSystemFonts: false,
                    enableXfa: false,
                    verbosityLevel: 0
                }
            );

            return pngPages[0].content;
        } catch (error: any) {
            console.error(error.message);
            throw error;
        }
    }

    public async convertEpubFileToPngBuffers(
        epubFile: Express.Multer.File
    ): Promise<Buffer[]> {
        try {
            const buffers: Buffer[] = [];

            const tempPath = `./${epubFile.originalname}`;
            fs.writeFileSync(tempPath, epubFile.buffer);

            const epub = new EPub(tempPath);

            await new Promise<void>((resolve, reject) => {
                epub.on("end", resolve);
                epub.on("error", reject);
                epub.parse();
            });

            const htmlFiles = Object.keys(epub.flow).map((key) => epub.flow[key].id);

            const browser = await puppeteer.launch();

            for (const htmlFile of htmlFiles) {
                const content = await new Promise<string>((resolve, reject) => {
                    epub.getChapter(htmlFile, (err, text) => {
                        if (err) reject(err);
                        else resolve(text);
                    });
                });

                const page = await browser.newPage();

                await page.setContent(content, { waitUntil: "networkidle0" });

                await page.setViewport({ width: 800, height: 1000 });

                const screenshotData = await page.screenshot({
                    type: "png",
                    fullPage: true
                });

                const buffer = Buffer.from(screenshotData);
                buffers.push(buffer);

                await page.close();
            }

            await browser.close();

            fs.unlinkSync(tempPath);

            return await this.splitPngBuffersByHeight(buffers, 800);
        } catch (error: any) {
            throw error;
        }
    }

    public async splitPngBuffersByHeight(
        buffers: Buffer[],
        splitHeight: number
    ): Promise<Buffer[]> {
        try {
            if (splitHeight <= 0) {
                throw new Error("splitHeight must be a positive number");
            }

            const splitBuffersArray = await Promise.all(
                buffers.map(async (buffer, index) => {
                    try {
                        const image = sharp(buffer);
                        const metadata = await image.metadata();

                        if (!metadata.height || !metadata.width) {
                            throw new Error(`Image at index ${index} is missing height or width metadata`);
                        }

                        const { height: totalHeight, width } = metadata;
                        const numSplits = Math.ceil(totalHeight / splitHeight);
                        const splits: Buffer[] = [];

                        for (let i = 0; i < numSplits; i++) {
                            const top = i * splitHeight;
                            const currentSplitHeight = Math.min(splitHeight, totalHeight - top);

                            try {
                                const partBuffer = await sharp(buffer)
                                    .extract({ left: 0, top, width, height: currentSplitHeight })
                                    .png()
                                    .toBuffer();

                                splits.push(partBuffer);
                            } catch (extractError) {
                                console.error(`Error extracting split ${i} for Image ${index}:`, extractError);
                            }
                        }

                        return splits;
                    } catch (error) {
                        console.error(`Error processing buffer at index ${index}:`, error);
                        return [];
                    }
                })
            );

            const flatSplitBuffers = splitBuffersArray.flat();

            return flatSplitBuffers;
        } catch (error: any) {
            throw error;
        }
    }

    public async convertJpegFileToPngBuffers(
        imageFile: Express.Multer.File
    ): Promise<Buffer> {
        try {
            const tempPath = `./${imageFile.originalname}`;

            fs.writeFileSync(tempPath, imageFile.buffer);

            const pngBuffer: Buffer = await sharp(tempPath)
                .png()
                .toBuffer();

            fs.unlinkSync(tempPath);

            return pngBuffer;
        } catch (error: any) {
            console.error(error.message);
            throw error;
        }
    }
}
