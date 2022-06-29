import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'field',
        data: { pageTitle: 'herveApp.field.home.title' },
        loadChildren: () => import('../pages/field/field.module').then(m => m.FieldModule),
      },
      {
        path: 'sensor',
        data: { pageTitle: 'herveApp.sensor.home.title' },
        loadChildren: () => import('../pages/sensor/sensor.module').then(m => m.SensorModule),
      },
      {
        path: 'sensor-data',
        data: { pageTitle: 'herveApp.sensorData.home.title' },
        loadChildren: () => import('../pages/sensor-data/sensor-data.module').then(m => m.SensorDataModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
