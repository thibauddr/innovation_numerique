import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SensorDataDetailComponent } from './detail/sensor-data-detail.component';
import { SensorDataRoutingModule } from './route/sensor-data-routing.module';

@NgModule({
  imports: [SharedModule, SensorDataRoutingModule],
  declarations: [SensorDataDetailComponent],
})
export class SensorDataModule {}
