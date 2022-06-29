import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SensorTypeService } from '../service/sensor-type.service';
import { ISensorType, SensorType } from '../sensor-type.model';

import { SensorTypeUpdateComponent } from './sensor-type-update.component';

describe('SensorType Management Update Component', () => {
  let comp: SensorTypeUpdateComponent;
  let fixture: ComponentFixture<SensorTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sensorTypeService: SensorTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SensorTypeUpdateComponent],
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
      .overrideTemplate(SensorTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensorTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sensorTypeService = TestBed.inject(SensorTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sensorType: ISensorType = { id: 456 };

      activatedRoute.data = of({ sensorType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sensorType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensorType>>();
      const sensorType = { id: 123 };
      jest.spyOn(sensorTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensorType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensorType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sensorTypeService.update).toHaveBeenCalledWith(sensorType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensorType>>();
      const sensorType = new SensorType();
      jest.spyOn(sensorTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensorType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensorType }));
      saveSubject.complete();

      // THEN
      expect(sensorTypeService.create).toHaveBeenCalledWith(sensorType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensorType>>();
      const sensorType = { id: 123 };
      jest.spyOn(sensorTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensorType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sensorTypeService.update).toHaveBeenCalledWith(sensorType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
