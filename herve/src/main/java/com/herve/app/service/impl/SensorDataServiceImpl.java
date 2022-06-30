package com.herve.app.service.impl;

import com.herve.app.domain.SensorData;
import com.herve.app.repository.SensorDataRepository;
import com.herve.app.service.SensorDataService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SensorData}.
 */
@Service
@Transactional
public class SensorDataServiceImpl implements SensorDataService {

    private final Logger log = LoggerFactory.getLogger(SensorDataServiceImpl.class);

    private final SensorDataRepository sensorDataRepository;

    public SensorDataServiceImpl(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @Override
    public SensorData save(SensorData sensorData) {
        log.debug("Request to save SensorData : {}", sensorData);
        return sensorDataRepository.save(sensorData);
    }

    @Override
    public SensorData update(SensorData sensorData) {
        log.debug("Request to save SensorData : {}", sensorData);
        return sensorDataRepository.save(sensorData);
    }

    @Override
    public Optional<SensorData> partialUpdate(SensorData sensorData) {
        log.debug("Request to partially update SensorData : {}", sensorData);

        return sensorDataRepository
            .findById(sensorData.getId())
            .map(existingSensorData -> {
                if (sensorData.getUnit() != null) {
                    existingSensorData.setUnit(sensorData.getUnit());
                }
                if (sensorData.getValue() != null) {
                    existingSensorData.setValue(sensorData.getValue());
                }
                if (sensorData.getDatetime() != null) {
                    existingSensorData.setDatetime(sensorData.getDatetime());
                }

                return existingSensorData;
            })
            .map(sensorDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorData> findAll() {
        log.debug("Request to get all SensorData");
        return sensorDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SensorData> findOne(Long id) {
        log.debug("Request to get SensorData : {}", id);
        return sensorDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorData : {}", id);
        sensorDataRepository.deleteById(id);
    }
}
