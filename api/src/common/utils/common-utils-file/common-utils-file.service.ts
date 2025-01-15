import { Injectable } from '@nestjs/common';

@Injectable()
export class CommonUtilsFileService {

  public getMimeType(fileExtension: string): string {
    const mimeTypes: { [key: string]: string } = {
      'jpeg': 'image/jpeg',
      'jpg': 'image/jpeg',
      'png': 'image/png',
      'pdf': 'application/pdf'
    };
    return mimeTypes[fileExtension.toLowerCase()] || 'application/octet-stream';
  }

}
