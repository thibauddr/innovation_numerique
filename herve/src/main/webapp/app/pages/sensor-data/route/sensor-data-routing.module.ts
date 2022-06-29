import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SensorDataDetailComponent } from '../detail/sensor-data-detail.component';
import { SensorDataRoutingResolveService } from './sensor-data-routing-resolve.service';

const sensorDataRoute: Routes = [
  {
    path: ':id/view',
    component: SensorDataDetailComponent,
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
