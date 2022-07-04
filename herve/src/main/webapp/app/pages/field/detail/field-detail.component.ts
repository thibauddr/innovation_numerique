import { formatDate } from '@angular/common';
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
  styleUrls: ['./field-detail.component.scss'],
})
export class FieldDetailComponent implements OnInit {
  field: IField | null = null;
  sensors: ISensor[] = [];
  lineData: any = {};
  now: Date = new Date();

  formatDate = 'dd/MM/yyyy';
  regionDate = 'fr-FR';

  temperatureData: any[] = [];
  LuminosityData: any[] = [];
  HumidityData: any[] = [];
  RaimData: any[] = [];


  $temperatureColor = '#F27A5E';
  $luminosityColor = '#F2E34D';
  $humidityColor = '#52BF04';
  $raimColor = '#60C5FF';

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
      .subscribe(() => {
        this.setLineData();
      });
  }

  private setLineData(): void {


    this.lineData = {
      labels: [
        formatDate(new Date().setDate(this.now.getDate() - 4), this.formatDate, this.regionDate),
        formatDate(new Date().setDate(this.now.getDate() - 3), this.formatDate, this.regionDate),
        formatDate(new Date().setDate(this.now.getDate() - 2), this.formatDate, this.regionDate),
        formatDate(new Date().setDate(this.now.getDate() - 1), this.formatDate, this.regionDate),
        formatDate(this.now, this.formatDate, this.regionDate),
      ],
      datasets: [
        {
          label: 'Température',
          data: [65, 59, 80, 81, 56, 55, 40],
          fill: false,
          tension: 0.4,
          borderColor: this.$temperatureColor,
        },
        {
          label: 'Luminosité',
          data: [28, 48, 40, 19, 86, 27, 90],
          fill: false,
          tension: 0.4,
          borderColor: this.$luminosityColor,
        },
        {
          label: 'Humidité',
          data: [28, 48, 40, 19, 86, 27, 90],
          fill: false,
          tension: 0.4,
          borderColor: this.$humidityColor,
        },
        {
          label: 'Pluie',
          data: [28, 48, 40, 19, 86, 27, 90],
          fill: false,
          tension: 0.4,
          borderColor: this.$raimColor,
        },
      ],
    };
  }
}
