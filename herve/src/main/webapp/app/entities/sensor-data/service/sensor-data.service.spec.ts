import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISensorData, SensorData } from '../sensor-data.model';

import { SensorDataService } from './sensor-data.service';

describe('SensorData Service', () => {
  let service: SensorDataService;
  let httpMock: HttpTestingController;
  let elemDefault: ISensorData;
  let expectedResult: ISensorData | ISensorData[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SensorDataService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      unit: 'AAAAAAA',
      value: 0,
      datetime: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          datetime: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SensorData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          datetime: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datetime: currentDate,
        },
        returnedFromService
      );

      service.create(new SensorData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SensorData', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          unit: 'BBBBBB',
          value: 1,
          datetime: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datetime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SensorData', () => {
      const patchObject = Object.assign(
        {
          datetime: currentDate.format(DATE_FORMAT),
        },
        new SensorData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          datetime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SensorData', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          unit: 'BBBBBB',
          value: 1,
          datetime: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          datetime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SensorData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSensorDataToCollectionIfMissing', () => {
      it('should add a SensorData to an empty array', () => {
        const sensorData: ISensorData = { id: 123 };
        expectedResult = service.addSensorDataToCollectionIfMissing([], sensorData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensorData);
      });

      it('should not add a SensorData to an array that contains it', () => {
        const sensorData: ISensorData = { id: 123 };
        const sensorDataCollection: ISensorData[] = [
          {
            ...sensorData,
          },
          { id: 456 },
        ];
        expectedResult = service.addSensorDataToCollectionIfMissing(sensorDataCollection, sensorData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SensorData to an array that doesn't contain it", () => {
        const sensorData: ISensorData = { id: 123 };
        const sensorDataCollection: ISensorData[] = [{ id: 456 }];
        expectedResult = service.addSensorDataToCollectionIfMissing(sensorDataCollection, sensorData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensorData);
      });

      it('should add only unique SensorData to an array', () => {
        const sensorDataArray: ISensorData[] = [{ id: 123 }, { id: 456 }, { id: 20547 }];
        const sensorDataCollection: ISensorData[] = [{ id: 123 }];
        expectedResult = service.addSensorDataToCollectionIfMissing(sensorDataCollection, ...sensorDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sensorData: ISensorData = { id: 123 };
        const sensorData2: ISensorData = { id: 456 };
        expectedResult = service.addSensorDataToCollectionIfMissing([], sensorData, sensorData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensorData);
        expect(expectedResult).toContain(sensorData2);
      });

      it('should accept null and undefined values', () => {
        const sensorData: ISensorData = { id: 123 };
        expectedResult = service.addSensorDataToCollectionIfMissing([], null, sensorData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensorData);
      });

      it('should return initial array if no SensorData is added', () => {
        const sensorDataCollection: ISensorData[] = [{ id: 123 }];
        expectedResult = service.addSensorDataToCollectionIfMissing(sensorDataCollection, undefined, null);
        expect(expectedResult).toEqual(sensorDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
