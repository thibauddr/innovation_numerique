import { formatDate } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';
import { Dayjs } from 'dayjs';
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
  now: Date = new Date();

  formatDate = 'dd/MM/yyyy';
  regionDate = 'fr-FR';

  temperatureData: any = {};
  luminosityData: any = {};
  humidityData: any = {};
  raimData: any = {};

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
        this.setLineTemperature();
        this.setLineLight();
        this.setLineHumidity();
        this.setLineRaim();
      });
  }

  private setLabelsLine(): any[] {
    return [
      formatDate(new Date().setDate(this.now.getDate() - 4), this.formatDate, this.regionDate),
      formatDate(new Date().setDate(this.now.getDate() - 3), this.formatDate, this.regionDate),
      formatDate(new Date().setDate(this.now.getDate() - 2), this.formatDate, this.regionDate),
      formatDate(new Date().setDate(this.now.getDate() - 1), this.formatDate, this.regionDate),
      formatDate(this.now, this.formatDate, this.regionDate),
    ];
  }

  private setLineTemperature(): void {
    let sensorsLength = 0;
    let min = 0;
    let max = 0;
    const values: number[] = [];
    const minDate = formatDate(new Date().setDate(this.now.getDate() - 4), this.formatDate, this.regionDate);
    this.sensors.forEach(sensor => {
      const typeCode = sensor ? (sensor.sensorType ? sensor.sensorType.code : -1) : -1;
      if (typeCode === 'TEM') {
        sensorsLength++;
        min += sensor.minThreshold ?? 0;
        max += sensor.threshold ?? 0;
        sensor.sensorData?.forEach(data => {
          if (data ? data.datetime : new Dayjs() <= new Dayjs(minDate)) {
            values.push(data.value ? data.value : 0);
          }
        });
      }
    });
    min = min / sensorsLength;
    max = max / sensorsLength;

    this.temperatureData = {
      labels: this.setLabelsLine(),
      datasets: [
        {
          label: 'Température',
          data: [values[0], values[1], values[2], values[3], values[4]],
          fill: false,
          tension: 0.4,
          borderColor: this.$temperatureColor,
        },
        {
          label: 'Seuil min',
          data: [min, min, min, min, min],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
        {
          label: 'Seuil max',
          data: [max, max, max, max, max],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
      ],
    };
  }

  private setLineLight(): void {
    let sensorsLength = 0;
    let min = 0;
    let max = 0;
    const values: number[] = [];
    const minDate = formatDate(new Date().setDate(this.now.getDate() - 4), this.formatDate, this.regionDate);
    this.sensors.forEach(sensor => {
      const typeCode = sensor ? (sensor.sensorType ? sensor.sensorType.code : -1) : -1;
      if (typeCode === 'LUM') {
        sensorsLength++;
        min += sensor.minThreshold ?? 0;
        max += sensor.threshold ?? 0;
        sensor.sensorData?.forEach(data => {
          if (data ? data.datetime : new Dayjs() <= new Dayjs(minDate)) {
            values.push(data.value ? data.value : 0);
          }
        });
      }
    });
    min = min / sensorsLength;
    max = max / sensorsLength;

    this.luminosityData = {
      labels: this.setLabelsLine(),
      datasets: [
        {
          label: 'Luminosity',
          data: [values[0], values[1], values[2], values[3], values[4]],
          fill: false,
          tension: 0.4,
          borderColor: this.$luminosityColor,
        },
        {
          label: 'Seuil min',
          data: [min, min, min, min, min],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
        {
          label: 'Seuil max',
          data: [max, max, max, max, max],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
      ],
    };
  }

  private setLineHumidity(): void {
    let sensorsLength = 0;
    let min = 0;
    let max = 0;
    const values: number[] = [];
    const minDate = formatDate(new Date().setDate(this.now.getDate() - 4), this.formatDate, this.regionDate);
    this.sensors.forEach(sensor => {
      const typeCode = sensor ? (sensor.sensorType ? sensor.sensorType.code : -1) : -1;
      if (typeCode === 'HUM') {
        sensorsLength++;
        min += sensor.minThreshold ?? 0;
        max += sensor.threshold ?? 0;
        sensor.sensorData?.forEach(data => {
          if (data ? data.datetime : new Dayjs() <= new Dayjs(minDate)) {
            values.push(data.value ? data.value : 0);
          }
        });
      }
    });
    min = min / sensorsLength;
    max = max / sensorsLength;

    this.humidityData = {
      labels: this.setLabelsLine(),
      datasets: [
        {
          label: 'Humidité',
          data: [values[0], values[1], values[2], values[3], values[4]],
          fill: false,
          tension: 0.4,
          borderColor: this.$humidityColor,
        },
        {
          label: 'Seuil min',
          data: [min, min, min, min, min],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
        {
          label: 'Seuil max',
          data: [max, max, max, max, max],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
      ],
    };
  }

  private setLineRaim(): void {
    let sensorsLength = 0;
    let min = 0;
    let max = 0;
    const values: number[] = [];
    const minDate = formatDate(new Date().setDate(this.now.getDate() - 4), this.formatDate, this.regionDate);
    this.sensors.forEach(sensor => {
      const typeCode = sensor ? (sensor.sensorType ? sensor.sensorType.code : -1) : -1;
      if (typeCode === 'RAI') {
        sensorsLength++;
        min += sensor.minThreshold ?? 0;
        max += sensor.threshold ?? 0;
        sensor.sensorData?.forEach(data => {
          if (data ? data.datetime : new Dayjs() <= new Dayjs(minDate)) {
            values.push(data.value ? data.value : 0);
          }
        });
      }
    });
    min = min / sensorsLength;
    max = max / sensorsLength;

    this.raimData = {
      labels: this.setLabelsLine(),
      datasets: [
        {
          label: 'Précipitations',
          data: [values[0], values[1], values[2], values[3], values[4]],
          fill: false,
          tension: 0.4,
          borderColor: this.$raimColor,
        },
        {
          label: 'Seuil min',
          data: [min, min, min, min, min],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
        {
          label: 'Seuil max',
          data: [max, max, max, max, max],
          fill: false,
          tension: 0.4,
          borderColor: '#000',
        },
      ],
    };
  }
}
