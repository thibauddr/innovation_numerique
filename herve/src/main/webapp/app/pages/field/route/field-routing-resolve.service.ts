import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IField, Field } from '../../../entities/field/field.model';
import { FieldService } from '../../../entities/field/service/field.service';

@Injectable({ providedIn: 'root' })
export class FieldRoutingResolveService implements Resolve<IField> {
  constructor(protected service: FieldService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IField> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((field: HttpResponse<Field>) => {
          if (field.body) {
            return of(field.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Field());
  }
}
