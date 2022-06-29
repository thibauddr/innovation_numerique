import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISensorType, SensorType } from '../sensor-type.model';

import { SensorTypeService } from './sensor-type.service';

describe('SensorType Service', () => {
  let service: SensorTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISensorType;
  let expectedResult: ISensorType | ISensorType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SensorTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
      name: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SensorType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SensorType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SensorType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SensorType', () => {
      const patchObject = Object.assign(
        {
          code: 'BBBBBB',
          description: 'BBBBBB',
        },
        new SensorType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SensorType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SensorType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSensorTypeToCollectionIfMissing', () => {
      it('should add a SensorType to an empty array', () => {
        const sensorType: ISensorType = { id: 123 };
        expectedResult = service.addSensorTypeToCollectionIfMissing([], sensorType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensorType);
      });

      it('should not add a SensorType to an array that contains it', () => {
        const sensorType: ISensorType = { id: 123 };
        const sensorTypeCollection: ISensorType[] = [
          {
            ...sensorType,
          },
          { id: 456 },
        ];
        expectedResult = service.addSensorTypeToCollectionIfMissing(sensorTypeCollection, sensorType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SensorType to an array that doesn't contain it", () => {
        const sensorType: ISensorType = { id: 123 };
        const sensorTypeCollection: ISensorType[] = [{ id: 456 }];
        expectedResult = service.addSensorTypeToCollectionIfMissing(sensorTypeCollection, sensorType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensorType);
      });

      it('should add only unique SensorType to an array', () => {
        const sensorTypeArray: ISensorType[] = [{ id: 123 }, { id: 456 }, { id: 90419 }];
        const sensorTypeCollection: ISensorType[] = [{ id: 123 }];
        expectedResult = service.addSensorTypeToCollectionIfMissing(sensorTypeCollection, ...sensorTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sensorType: ISensorType = { id: 123 };
        const sensorType2: ISensorType = { id: 456 };
        expectedResult = service.addSensorTypeToCollectionIfMissing([], sensorType, sensorType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensorType);
        expect(expectedResult).toContain(sensorType2);
      });

      it('should accept null and undefined values', () => {
        const sensorType: ISensorType = { id: 123 };
        expectedResult = service.addSensorTypeToCollectionIfMissing([], null, sensorType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensorType);
      });

      it('should return initial array if no SensorType is added', () => {
        const sensorTypeCollection: ISensorType[] = [{ id: 123 }];
        expectedResult = service.addSensorTypeToCollectionIfMissing(sensorTypeCollection, undefined, null);
        expect(expectedResult).toEqual(sensorTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
