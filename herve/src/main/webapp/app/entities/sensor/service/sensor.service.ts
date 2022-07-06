import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISensor, getSensorIdentifier } from '../sensor.model';

export type EntityResponseType = HttpResponse<ISensor>;
export type EntityArrayResponseType = HttpResponse<ISensor[]>;

@Injectable({ providedIn: 'root' })
export class SensorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sensors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sensor: ISensor): Observable<EntityResponseType> {
    return this.http.post<ISensor>(this.resourceUrl, sensor, { observe: 'response' });
  }

  update(sensor: ISensor): Observable<EntityResponseType> {
    return this.http.put<ISensor>(`${this.resourceUrl}/${getSensorIdentifier(sensor) as number}`, sensor, { observe: 'response' });
  }

  partialUpdate(sensor: ISensor): Observable<EntityResponseType> {
    return this.http.patch<ISensor>(`${this.resourceUrl}/${getSensorIdentifier(sensor) as number}`, sensor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISensor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWithFieldId(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<ISensor[]>(`${this.resourceUrl}/getWithFieldId/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISensor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSensorToCollectionIfMissing(sensorCollection: ISensor[], ...sensorsToCheck: (ISensor | null | undefined)[]): ISensor[] {
    const sensors: ISensor[] = sensorsToCheck.filter(isPresent);
    if (sensors.length > 0) {
      const sensorCollectionIdentifiers = sensorCollection.map(sensorItem => getSensorIdentifier(sensorItem)!);
      const sensorsToAdd = sensors.filter(sensorItem => {
        const sensorIdentifier = getSensorIdentifier(sensorItem);
        if (sensorIdentifier == null || sensorCollectionIdentifiers.includes(sensorIdentifier)) {
          return false;
        }
        sensorCollectionIdentifiers.push(sensorIdentifier);
        return true;
      });
      return [...sensorsToAdd, ...sensorCollection];
    }
    return sensorCollection;
  }
}
