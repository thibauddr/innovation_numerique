import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISensorData } from '../sensor-data.model';

@Component({
  selector: 'jhi-sensor-data-detail',
  templateUrl: './sensor-data-detail.component.html',
})
export class SensorDataDetailComponent implements OnInit {
  sensorData: ISensorData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensorData }) => {
      this.sensorData = sensorData;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
