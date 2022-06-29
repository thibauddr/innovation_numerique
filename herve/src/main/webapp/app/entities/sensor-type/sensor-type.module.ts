import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SensorTypeComponent } from './list/sensor-type.component';
import { SensorTypeDetailComponent } from './detail/sensor-type-detail.component';
import { SensorTypeUpdateComponent } from './update/sensor-type-update.component';
import { SensorTypeDeleteDialogComponent } from './delete/sensor-type-delete-dialog.component';
import { SensorTypeRoutingModule } from './route/sensor-type-routing.module';

@NgModule({
  imports: [SharedModule, SensorTypeRoutingModule],
  declarations: [SensorTypeComponent, SensorTypeDetailComponent, SensorTypeUpdateComponent, SensorTypeDeleteDialogComponent],
  entryComponents: [SensorTypeDeleteDialogComponent],
})
export class SensorTypeModule {}
