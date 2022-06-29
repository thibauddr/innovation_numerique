import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SensorDataComponent } from '../list/sensor-data.component';
import { SensorDataDetailComponent } from '../detail/sensor-data-detail.component';
import { SensorDataUpdateComponent } from '../update/sensor-data-update.component';
import { SensorDataRoutingResolveService } from './sensor-data-routing-resolve.service';

const sensorDataRoute: Routes = [
  {
    path: '',
    component: SensorDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SensorDataDetailComponent,
    resolve: {
      sensorData: SensorDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SensorDataUpdateComponent,
    resolve: {
      sensorData: SensorDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SensorDataUpdateComponent,
    resolve: {
      sensorData: SensorDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sensorDataRoute)],
  exports: [RouterModule],
})
export class SensorDataRoutingModule {}
