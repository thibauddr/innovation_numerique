import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISensor, Sensor } from '../sensor.model';

import { SensorService } from './sensor.service';

describe('Sensor Service', () => {
  let service: SensorService;
  let httpMock: HttpTestingController;
  let elemDefault: ISensor;
  let expectedResult: ISensor | ISensor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SensorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      position_x: 0,
      position_y: 0,
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

    it('should create a Sensor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Sensor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sensor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          position_x: 1,
          position_y: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sensor', () => {
      const patchObject = Object.assign(
        {
          position_x: 1,
        },
        new Sensor()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sensor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          position_x: 1,
          position_y: 1,
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

    it('should delete a Sensor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSensorToCollectionIfMissing', () => {
      it('should add a Sensor to an empty array', () => {
        const sensor: ISensor = { id: 123 };
        expectedResult = service.addSensorToCollectionIfMissing([], sensor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensor);
      });

      it('should not add a Sensor to an array that contains it', () => {
        const sensor: ISensor = { id: 123 };
        const sensorCollection: ISensor[] = [
          {
            ...sensor,
          },
          { id: 456 },
        ];
        expectedResult = service.addSensorToCollectionIfMissing(sensorCollection, sensor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sensor to an array that doesn't contain it", () => {
        const sensor: ISensor = { id: 123 };
        const sensorCollection: ISensor[] = [{ id: 456 }];
        expectedResult = service.addSensorToCollectionIfMissing(sensorCollection, sensor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensor);
      });

      it('should add only unique Sensor to an array', () => {
        const sensorArray: ISensor[] = [{ id: 123 }, { id: 456 }, { id: 46845 }];
        const sensorCollection: ISensor[] = [{ id: 123 }];
        expectedResult = service.addSensorToCollectionIfMissing(sensorCollection, ...sensorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sensor: ISensor = { id: 123 };
        const sensor2: ISensor = { id: 456 };
        expectedResult = service.addSensorToCollectionIfMissing([], sensor, sensor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensor);
        expect(expectedResult).toContain(sensor2);
      });

      it('should accept null and undefined values', () => {
        const sensor: ISensor = { id: 123 };
        expectedResult = service.addSensorToCollectionIfMissing([], null, sensor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensor);
      });

      it('should return initial array if no Sensor is added', () => {
        const sensorCollection: ISensor[] = [{ id: 123 }];
        expectedResult = service.addSensorToCollectionIfMissing(sensorCollection, undefined, null);
        expect(expectedResult).toEqual(sensorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
