import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SensorDataComponent } from './list/sensor-data.component';
import { SensorDataDetailComponent } from './detail/sensor-data-detail.component';
import { SensorDataUpdateComponent } from './update/sensor-data-update.component';
import { SensorDataDeleteDialogComponent } from './delete/sensor-data-delete-dialog.component';
import { SensorDataRoutingModule } from './route/sensor-data-routing.module';

@NgModule({
  imports: [SharedModule, SensorDataRoutingModule],
  declarations: [SensorDataComponent, SensorDataDetailComponent, SensorDataUpdateComponent, SensorDataDeleteDialogComponent],
  entryComponents: [SensorDataDeleteDialogComponent],
})
export class SensorDataModule {}
