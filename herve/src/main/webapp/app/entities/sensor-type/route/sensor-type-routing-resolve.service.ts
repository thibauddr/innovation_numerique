import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISensorType, SensorType } from '../sensor-type.model';
import { SensorTypeService } from '../service/sensor-type.service';

@Injectable({ providedIn: 'root' })
export class SensorTypeRoutingResolveService implements Resolve<ISensorType> {
  constructor(protected service: SensorTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISensorType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sensorType: HttpResponse<SensorType>) => {
          if (sensorType.body) {
            return of(sensorType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SensorType());
  }
}
