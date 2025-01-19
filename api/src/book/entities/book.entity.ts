export class Book { }

export type BookInfoAndData = {
    pages: (
        {
            file: {
                name: string;
                url: string;
            };
        } & {
            id: string;
            order: number;
            bookId: string;
            fileId: string;
        }
    )[];
} & {
    id: string;
    createdAt: Date;
    title: string;
    authorId: string;
    coverId: string;
}
