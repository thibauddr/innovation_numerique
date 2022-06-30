package com.herve.app.service.impl;

import com.herve.app.domain.Sensor;
import com.herve.app.repository.SensorRepository;
import com.herve.app.service.SensorService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sensor}.
 */
@Service
@Transactional
public class SensorServiceImpl implements SensorService {

    private final Logger log = LoggerFactory.getLogger(SensorServiceImpl.class);

    private final SensorRepository sensorRepository;

    public SensorServiceImpl(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Sensor save(Sensor sensor) {
        log.debug("Request to save Sensor : {}", sensor);
        return sensorRepository.save(sensor);
    }

    @Override
    public Sensor update(Sensor sensor) {
        log.debug("Request to save Sensor : {}", sensor);
        return sensorRepository.save(sensor);
    }

    @Override
    public Optional<Sensor> partialUpdate(Sensor sensor) {
        log.debug("Request to partially update Sensor : {}", sensor);

        return sensorRepository
            .findById(sensor.getId())
            .map(existingSensor -> {
                if (sensor.getName() != null) {
                    existingSensor.setName(sensor.getName());
                }
                if (sensor.getDescription() != null) {
                    existingSensor.setDescription(sensor.getDescription());
                }
                if (sensor.getPosition_x() != null) {
                    existingSensor.setPosition_x(sensor.getPosition_x());
                }
                if (sensor.getPosition_y() != null) {
                    existingSensor.setPosition_y(sensor.getPosition_y());
                }

                return existingSensor;
            })
            .map(sensorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sensor> findAll() {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sensor> findOne(Long id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.deleteById(id);
    }
}
