import { Injectable } from '@nestjs/common';
import { CreatePageDto } from './dto/create-page.dto';
import { UpdatePageDto } from './dto/update-page.dto';
import { v4 } from 'uuid';

@Injectable()
export class PageService {
    public async createPage(
        dto: CreatePageDto
    ): Promise<Page> {
        
    }
}
