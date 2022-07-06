import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SensorComponent } from './list/sensor.component';
import { SensorDetailComponent } from './detail/sensor-detail.component';
import { SensorUpdateComponent } from './update/sensor-update.component';
import { SensorDeleteDialogComponent } from './delete/sensor-delete-dialog.component';
import { SensorRoutingModule } from './route/sensor-routing.module';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ChipModule } from 'primeng/chip';

@NgModule({
  imports: [SharedModule, SensorRoutingModule, TableModule, ButtonModule, ChipModule],
  declarations: [SensorComponent, SensorDetailComponent, SensorUpdateComponent, SensorDeleteDialogComponent],
  entryComponents: [SensorDeleteDialogComponent],
})
export class SensorModule {}
