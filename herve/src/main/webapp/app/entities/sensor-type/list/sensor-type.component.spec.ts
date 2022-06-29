import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SensorTypeService } from '../service/sensor-type.service';

import { SensorTypeComponent } from './sensor-type.component';

describe('SensorType Management Component', () => {
  let comp: SensorTypeComponent;
  let fixture: ComponentFixture<SensorTypeComponent>;
  let service: SensorTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SensorTypeComponent],
    })
      .overrideTemplate(SensorTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensorTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SensorTypeService);

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
    expect(comp.sensorTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
