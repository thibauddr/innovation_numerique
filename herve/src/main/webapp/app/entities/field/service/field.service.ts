import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IField, getFieldIdentifier } from '../field.model';

export type EntityResponseType = HttpResponse<IField>;
export type EntityArrayResponseType = HttpResponse<IField[]>;

@Injectable({ providedIn: 'root' })
export class FieldService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fields');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(field: IField): Observable<EntityResponseType> {
    return this.http.post<IField>(this.resourceUrl, field, { observe: 'response' });
  }

  update(field: IField): Observable<EntityResponseType> {
    return this.http.put<IField>(`${this.resourceUrl}/${getFieldIdentifier(field) as number}`, field, { observe: 'response' });
  }

  partialUpdate(field: IField): Observable<EntityResponseType> {
    return this.http.patch<IField>(`${this.resourceUrl}/${getFieldIdentifier(field) as number}`, field, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IField>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IField[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getWithCurrentUser(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IField[]>(`${this.resourceUrl}/getWithCurrentUser`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFieldToCollectionIfMissing(fieldCollection: IField[], ...fieldsToCheck: (IField | null | undefined)[]): IField[] {
    const fields: IField[] = fieldsToCheck.filter(isPresent);
    if (fields.length > 0) {
      const fieldCollectionIdentifiers = fieldCollection.map(fieldItem => getFieldIdentifier(fieldItem)!);
      const fieldsToAdd = fields.filter(fieldItem => {
        const fieldIdentifier = getFieldIdentifier(fieldItem);
        if (fieldIdentifier == null || fieldCollectionIdentifiers.includes(fieldIdentifier)) {
          return false;
        }
        fieldCollectionIdentifiers.push(fieldIdentifier);
        return true;
      });
      return [...fieldsToAdd, ...fieldCollection];
    }
    return fieldCollection;
  }
}
