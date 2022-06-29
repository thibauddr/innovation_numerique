import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISensorData, SensorData } from '../sensor-data.model';
import { SensorDataService } from '../service/sensor-data.service';

@Injectable({ providedIn: 'root' })
export class SensorDataRoutingResolveService implements Resolve<ISensorData> {
  constructor(protected service: SensorDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISensorData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sensorData: HttpResponse<SensorData>) => {
          if (sensorData.body) {
            return of(sensorData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SensorData());
  }
}
