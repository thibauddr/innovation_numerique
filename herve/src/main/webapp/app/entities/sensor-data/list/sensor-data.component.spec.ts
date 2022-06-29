import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SensorDataService } from '../service/sensor-data.service';

import { SensorDataComponent } from './sensor-data.component';

describe('SensorData Management Component', () => {
  let comp: SensorDataComponent;
  let fixture: ComponentFixture<SensorDataComponent>;
  let service: SensorDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SensorDataComponent],
    })
      .overrideTemplate(SensorDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensorDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SensorDataService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.sensorData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
