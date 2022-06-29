import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISensorType, getSensorTypeIdentifier } from '../sensor-type.model';

export type EntityResponseType = HttpResponse<ISensorType>;
export type EntityArrayResponseType = HttpResponse<ISensorType[]>;

@Injectable({ providedIn: 'root' })
export class SensorTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sensor-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sensorType: ISensorType): Observable<EntityResponseType> {
    return this.http.post<ISensorType>(this.resourceUrl, sensorType, { observe: 'response' });
  }

  update(sensorType: ISensorType): Observable<EntityResponseType> {
    return this.http.put<ISensorType>(`${this.resourceUrl}/${getSensorTypeIdentifier(sensorType) as number}`, sensorType, {
      observe: 'response',
    });
  }

  partialUpdate(sensorType: ISensorType): Observable<EntityResponseType> {
    return this.http.patch<ISensorType>(`${this.resourceUrl}/${getSensorTypeIdentifier(sensorType) as number}`, sensorType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISensorType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISensorType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSensorTypeToCollectionIfMissing(
    sensorTypeCollection: ISensorType[],
    ...sensorTypesToCheck: (ISensorType | null | undefined)[]
  ): ISensorType[] {
    const sensorTypes: ISensorType[] = sensorTypesToCheck.filter(isPresent);
    if (sensorTypes.length > 0) {
      const sensorTypeCollectionIdentifiers = sensorTypeCollection.map(sensorTypeItem => getSensorTypeIdentifier(sensorTypeItem)!);
      const sensorTypesToAdd = sensorTypes.filter(sensorTypeItem => {
        const sensorTypeIdentifier = getSensorTypeIdentifier(sensorTypeItem);
        if (sensorTypeIdentifier == null || sensorTypeCollectionIdentifiers.includes(sensorTypeIdentifier)) {
          return false;
        }
        sensorTypeCollectionIdentifiers.push(sensorTypeIdentifier);
        return true;
      });
      return [...sensorTypesToAdd, ...sensorTypeCollection];
    }
    return sensorTypeCollection;
  }
}
