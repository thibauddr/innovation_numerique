import { IUser } from 'app/entities/user/user.model';
import { ISensor } from 'app/entities/sensor/sensor.model';

export interface IField {
  id?: number;
  name?: string | null;
  description?: string | null;
  position?: string | null;
  user?: IUser | null;
  sensors?: ISensor[] | null;
}

export class Field implements IField {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public position?: string | null,
    public user?: IUser | null,
    public sensors?: ISensor[] | null
  ) {}
}

export function getFieldIdentifier(field: IField): number | undefined {
  return field.id;
}
