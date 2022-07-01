import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISensorData, getSensorDataIdentifier } from '../sensor-data.model';

export type EntityResponseType = HttpResponse<ISensorData>;
export type EntityArrayResponseType = HttpResponse<ISensorData[]>;

@Injectable({ providedIn: 'root' })
export class SensorDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sensor-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sensorData: ISensorData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensorData);
    return this.http
      .post<ISensorData>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sensorData: ISensorData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensorData);
    return this.http
      .put<ISensorData>(`${this.resourceUrl}/${getSensorDataIdentifier(sensorData) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sensorData: ISensorData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensorData);
    return this.http
      .patch<ISensorData>(`${this.resourceUrl}/${getSensorDataIdentifier(sensorData) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISensorData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findNow(): Observable<EntityArrayResponseType> {
    return this.http
      .get<ISensorData[]>(`${this.resourceUrl}/now`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISensorData[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSensorDataToCollectionIfMissing(
    sensorDataCollection: ISensorData[],
    ...sensorDataToCheck: (ISensorData | null | undefined)[]
  ): ISensorData[] {
    const sensorData: ISensorData[] = sensorDataToCheck.filter(isPresent);
    if (sensorData.length > 0) {
      const sensorDataCollectionIdentifiers = sensorDataCollection.map(sensorDataItem => getSensorDataIdentifier(sensorDataItem)!);
      const sensorDataToAdd = sensorData.filter(sensorDataItem => {
        const sensorDataIdentifier = getSensorDataIdentifier(sensorDataItem);
        if (sensorDataIdentifier == null || sensorDataCollectionIdentifiers.includes(sensorDataIdentifier)) {
          return false;
        }
        sensorDataCollectionIdentifiers.push(sensorDataIdentifier);
        return true;
      });
      return [...sensorDataToAdd, ...sensorDataCollection];
    }
    return sensorDataCollection;
  }

  protected convertDateFromClient(sensorData: ISensorData): ISensorData {
    return Object.assign({}, sensorData, {
      datetime: sensorData.datetime?.isValid() ? sensorData.datetime.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datetime = res.body.datetime ? dayjs(res.body.datetime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sensorData: ISensorData) => {
        sensorData.datetime = sensorData.datetime ? dayjs(sensorData.datetime) : undefined;
      });
    }
    return res;
  }
}
