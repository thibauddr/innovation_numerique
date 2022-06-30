import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IField } from 'app/entities/field/field.model';
import { FieldService } from 'app/entities/field/service/field.service';
import { ISensorType } from 'app/entities/sensor-type/sensor-type.model';
import { SensorTypeService } from 'app/entities/sensor-type/service/sensor-type.service';

import { SensorUpdateComponent } from './sensor-update.component';
import { SensorService } from 'app/entities/sensor/service/sensor.service';
import { ISensor, Sensor } from 'app/entities/sensor/sensor.model';

describe('Sensor Management Update Component', () => {
  let comp: SensorUpdateComponent;
  let fixture: ComponentFixture<SensorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sensorService: SensorService;
  let fieldService: FieldService;
  let sensorTypeService: SensorTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SensorUpdateComponent],
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
      .overrideTemplate(SensorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sensorService = TestBed.inject(SensorService);
    fieldService = TestBed.inject(FieldService);
    sensorTypeService = TestBed.inject(SensorTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Field query and add missing value', () => {
      const sensor: ISensor = { id: 456 };
      const field: IField = { id: 11398 };
      sensor.field = field;

      const fieldCollection: IField[] = [{ id: 8536 }];
      jest.spyOn(fieldService, 'query').mockReturnValue(of(new HttpResponse({ body: fieldCollection })));
      const additionalFields = [field];
      const expectedCollection: IField[] = [...additionalFields, ...fieldCollection];
      jest.spyOn(fieldService, 'addFieldToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      expect(fieldService.query).toHaveBeenCalled();
      expect(fieldService.addFieldToCollectionIfMissing).toHaveBeenCalledWith(fieldCollection, ...additionalFields);
      expect(comp.fieldsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SensorType query and add missing value', () => {
      const sensor: ISensor = { id: 456 };
      const sensorType: ISensorType = { id: 51939 };
      sensor.sensorType = sensorType;

      const sensorTypeCollection: ISensorType[] = [{ id: 97600 }];
      jest.spyOn(sensorTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: sensorTypeCollection })));
      const additionalSensorTypes = [sensorType];
      const expectedCollection: ISensorType[] = [...additionalSensorTypes, ...sensorTypeCollection];
      jest.spyOn(sensorTypeService, 'addSensorTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      expect(sensorTypeService.query).toHaveBeenCalled();
      expect(sensorTypeService.addSensorTypeToCollectionIfMissing).toHaveBeenCalledWith(sensorTypeCollection, ...additionalSensorTypes);
      expect(comp.sensorTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sensor: ISensor = { id: 456 };
      const field: IField = { id: 29489 };
      sensor.field = field;
      const sensorType: ISensorType = { id: 27902 };
      sensor.sensorType = sensorType;

      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sensor));
      expect(comp.fieldsSharedCollection).toContain(field);
      expect(comp.sensorTypesSharedCollection).toContain(sensorType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sensor>>();
      const sensor = { id: 123 };
      jest.spyOn(sensorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sensorService.update).toHaveBeenCalledWith(sensor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sensor>>();
      const sensor = new Sensor();
      jest.spyOn(sensorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensor }));
      saveSubject.complete();

      // THEN
      expect(sensorService.create).toHaveBeenCalledWith(sensor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sensor>>();
      const sensor = { id: 123 };
      jest.spyOn(sensorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sensorService.update).toHaveBeenCalledWith(sensor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFieldById', () => {
      it('Should return tracked Field primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFieldById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSensorTypeById', () => {
      it('Should return tracked SensorType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSensorTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
