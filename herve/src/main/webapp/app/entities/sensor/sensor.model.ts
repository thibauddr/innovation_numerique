import { IField } from 'app/entities/field/field.model';
import { ISensorType } from 'app/entities/sensor-type/sensor-type.model';
import { ISensorData } from 'app/entities/sensor-data/sensor-data.model';

export interface ISensor {
  id?: number;
  name?: string | null;
  description?: string | null;
  position_x?: number | null;
  position_y?: number | null;
  field?: IField | null;
  sensorType?: ISensorType;
  sensorData?: ISensorData[] | null;
}

export class Sensor implements ISensor {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public position_x?: number | null,
    public position_y?: number | null,
    public field?: IField | null,
    public sensorType?: ISensorType,
    public sensorData?: ISensorData[] | null
  ) {}
}

export function getSensorIdentifier(sensor: ISensor): number | undefined {
  return sensor.id;
}
