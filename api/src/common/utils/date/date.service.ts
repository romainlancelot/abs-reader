import { Injectable } from '@nestjs/common';

@Injectable()
export class DateService {
  public async isDateWithinOpeningHours(date: Date): Promise<boolean> {
    const day = date.getUTCDay();
    const hour = date.getUTCHours();
    return day >= 1 && day <= 5 && hour >= 9 && hour < 20;
  }
}
