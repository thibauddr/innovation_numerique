import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { FieldService } from 'app/entities/field/service/field.service';
import { IField } from 'app/entities/field/field.model';
import { HttpResponse } from '@angular/common/http';
import { TEMPERATURE_SCALE } from 'app/entities/sensor/data/temperatureScale';
import { HUMIDITY_SCALE } from 'app/entities/sensor/data/humidityScale';
import { Message } from 'primeng/api';
import { SensorDataService } from 'app/entities/sensor-data/service/sensor-data.service';
import { ISensorData, SensorData } from 'app/entities/sensor-data/sensor-data.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  date = Date.now();

  account: Account | null = null;
  fields: IField[] = [];
  msgs: Message[] = [];

  alertToday: any[] = [];
  alertYesterday: any[] = [];

  TEMPERATURE_SCALE = TEMPERATURE_SCALE;
  HUMIDITY_SCALE = HUMIDITY_SCALE;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private fieldService: FieldService,
    private sensorDataService: SensorDataService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.setData();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  viewFieldPage(id: number): void {
    this.router.navigate(['/field/' + id + '/view']);
  }

  private setData(): void {
    this.fieldService
      .getWithCurrentUser()
      .pipe(
        map((res: HttpResponse<IField[]>) => {
          this.fields = res.body ?? [];
        })
      )
      .subscribe(() => {
        this.setSensors();
      });
  }

  private setSensors(): void {
    this.sensorDataService
      .getSensorDataCurrentUserAlert()
      .pipe(
        map((res: HttpResponse<ISensorData[]>) => {
          const alerts = res.body ?? [];
          alerts.forEach(alert => {
            const value = alert.value ? alert.value : -1;
            const minThreshold = alert ? (alert.sensor ? (alert.sensor.minThreshold ? alert.sensor.minThreshold : -1) : -1) : -1;
            const maxThreshold = alert ? (alert.sensor ? (alert.sensor.threshold ? alert.sensor.threshold : -1) : -1) : -1;

            if (value < minThreshold || value > maxThreshold) {
              this.alertToday.push(alert);
            }
          });
        })
      )
      .subscribe();

    const date = new Date();
    this.sensorDataService
      .getSensorDataCurrentUserAlertWithDate(new Date(date.setDate(date.getDate() - 1)))
      .pipe(
        map((res: HttpResponse<ISensorData[]>) => {
          this.alertYesterday = res.body ?? [];
        })
      )
      .subscribe();
  }
}
