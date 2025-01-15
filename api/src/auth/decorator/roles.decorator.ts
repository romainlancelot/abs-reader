import { CustomDecorator, SetMetadata } from '@nestjs/common';
import { Role } from '@prisma/client';

export const ROLES_KEY = 'roles';
export function Roles(...roles: Role[]): CustomDecorator<string> {
  return SetMetadata(ROLES_KEY, roles);
}
