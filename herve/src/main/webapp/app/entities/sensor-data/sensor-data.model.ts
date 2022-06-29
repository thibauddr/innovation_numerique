import dayjs from 'dayjs/esm';
import { ISensor } from 'app/entities/sensor/sensor.model';

export interface ISensorData {
  id?: number;
  unit?: string;
  value?: number;
  datetime?: dayjs.Dayjs | null;
  sensor?: ISensor | null;
}

export class SensorData implements ISensorData {
  constructor(
    public id?: number,
    public unit?: string,
    public value?: number,
    public datetime?: dayjs.Dayjs | null,
    public sensor?: ISensor | null
  ) {}
}

export function getSensorDataIdentifier(sensorData: ISensorData): number | undefined {
  return sensorData.id;
}
