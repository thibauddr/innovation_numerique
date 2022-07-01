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
import { SensorDataService } from 'app/entities/sensor-data/service/sensor-data.service';
import { ISensorData } from 'app/entities/sensor-data/sensor-data.model';
import { Message } from 'primeng/api';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  date = Date.now();

  account: Account | null = null;
  fields: IField[] = [];
  sensorData: ISensorData[] = [];
  msgs: Message[] = [];

  TEMPERATURE_SCALE = TEMPERATURE_SCALE;
  HUMIDITY_SCALE = HUMIDITY_SCALE;

  val = 4;

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

    this.setFields();
    this.setDataSensor();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private setFields(): void {
    this.fieldService
      .getWithCurrentUser()
      .pipe(
        map((res: HttpResponse<IField[]>) => {
          this.fields = res.body ?? [];
        })
      )
      .subscribe();
  }

  private setDataSensor(): void {
    this.sensorDataService
      .findNow()
      .pipe(
        map((res: HttpResponse<IField[]>) => {
          this.sensorData = res.body ?? [];
        })
      )
      .subscribe();
  }
}
