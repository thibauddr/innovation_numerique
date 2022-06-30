export interface ISensorType {
  id?: number;
  code?: string | null;
  name?: string | null;
  description?: string | null;
}

export class SensorType implements ISensorType {
  constructor(public id?: number, public code?: string | null, public name?: string | null, public description?: string | null) {}
}

export function getSensorTypeIdentifier(sensorType: ISensorType): number | undefined {
  return sensorType.id;
}
