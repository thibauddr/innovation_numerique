import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SensorTypeComponent } from '../list/sensor-type.component';
import { SensorTypeDetailComponent } from '../detail/sensor-type-detail.component';
import { SensorTypeUpdateComponent } from '../update/sensor-type-update.component';
import { SensorTypeRoutingResolveService } from './sensor-type-routing-resolve.service';

const sensorTypeRoute: Routes = [
  {
    path: '',
    component: SensorTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SensorTypeDetailComponent,
    resolve: {
      sensorType: SensorTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SensorTypeUpdateComponent,
    resolve: {
      sensorType: SensorTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SensorTypeUpdateComponent,
    resolve: {
      sensorType: SensorTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sensorTypeRoute)],
  exports: [RouterModule],
})
export class SensorTypeRoutingModule {}
