import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';
import { map } from 'rxjs';
import { IField } from '../../../entities/field/field.model';

@Component({
  selector: 'jhi-field-detail',
  templateUrl: './field-detail.component.html',
})
export class FieldDetailComponent implements OnInit {
  field: IField | null = null;
  sensors: ISensor[] = [];

  constructor(protected activatedRoute: ActivatedRoute, private router: Router, private sensorService: SensorService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ field }) => {
      this.field = field;
      this.setSensor();
    });
  }

  previousState(): void {
    window.history.back();
  }

  viewSensorPage(id: number): void {
    this.router.navigate(['/sensor/' + id + '/view']);
  }

  private setSensor(): void {
    this.sensorService
      .getWithFieldId(this.field?.id ? this.field?.id : -1)
      .pipe(
        map((res: HttpResponse<IField[]>) => {
          this.sensors = res.body ?? [];
        })
      )
      .subscribe();
  }
}
