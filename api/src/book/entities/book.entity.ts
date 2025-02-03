import { Book, Page, User } from "@prisma/client";

export interface BookWithPages extends Book {
    author: Partial<User>;
    pages: Partial<Page[]>;
}

export interface BookDetails {
    book: BookWithPages;
    isTheReaderTheAuthor: boolean;
}
