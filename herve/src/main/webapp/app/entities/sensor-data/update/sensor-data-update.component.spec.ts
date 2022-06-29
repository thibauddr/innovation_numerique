import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SensorDataService } from '../service/sensor-data.service';
import { ISensorData, SensorData } from '../sensor-data.model';
import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';

import { SensorDataUpdateComponent } from './sensor-data-update.component';

describe('SensorData Management Update Component', () => {
  let comp: SensorDataUpdateComponent;
  let fixture: ComponentFixture<SensorDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sensorDataService: SensorDataService;
  let sensorService: SensorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SensorDataUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SensorDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensorDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sensorDataService = TestBed.inject(SensorDataService);
    sensorService = TestBed.inject(SensorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sensor query and add missing value', () => {
      const sensorData: ISensorData = { id: 456 };
      const sensor: ISensor = { id: 22874 };
      sensorData.sensor = sensor;

      const sensorCollection: ISensor[] = [{ id: 29994 }];
      jest.spyOn(sensorService, 'query').mockReturnValue(of(new HttpResponse({ body: sensorCollection })));
      const additionalSensors = [sensor];
      const expectedCollection: ISensor[] = [...additionalSensors, ...sensorCollection];
      jest.spyOn(sensorService, 'addSensorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sensorData });
      comp.ngOnInit();

      expect(sensorService.query).toHaveBeenCalled();
      expect(sensorService.addSensorToCollectionIfMissing).toHaveBeenCalledWith(sensorCollection, ...additionalSensors);
      expect(comp.sensorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sensorData: ISensorData = { id: 456 };
      const sensor: ISensor = { id: 35945 };
      sensorData.sensor = sensor;

      activatedRoute.data = of({ sensorData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sensorData));
      expect(comp.sensorsSharedCollection).toContain(sensor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensorData>>();
      const sensorData = { id: 123 };
      jest.spyOn(sensorDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensorData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensorData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sensorDataService.update).toHaveBeenCalledWith(sensorData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensorData>>();
      const sensorData = new SensorData();
      jest.spyOn(sensorDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensorData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensorData }));
      saveSubject.complete();

      // THEN
      expect(sensorDataService.create).toHaveBeenCalledWith(sensorData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensorData>>();
      const sensorData = { id: 123 };
      jest.spyOn(sensorDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensorData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sensorDataService.update).toHaveBeenCalledWith(sensorData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSensorById', () => {
      it('Should return tracked Sensor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSensorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
