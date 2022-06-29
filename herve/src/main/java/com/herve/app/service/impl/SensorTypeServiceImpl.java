package com.herve.app.service.impl;

import com.herve.app.domain.SensorType;
import com.herve.app.repository.SensorTypeRepository;
import com.herve.app.service.SensorTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SensorType}.
 */
@Service
@Transactional
public class SensorTypeServiceImpl implements SensorTypeService {

    private final Logger log = LoggerFactory.getLogger(SensorTypeServiceImpl.class);

    private final SensorTypeRepository sensorTypeRepository;

    public SensorTypeServiceImpl(SensorTypeRepository sensorTypeRepository) {
        this.sensorTypeRepository = sensorTypeRepository;
    }

    @Override
    public SensorType save(SensorType sensorType) {
        log.debug("Request to save SensorType : {}", sensorType);
        return sensorTypeRepository.save(sensorType);
    }

    @Override
    public SensorType update(SensorType sensorType) {
        log.debug("Request to save SensorType : {}", sensorType);
        return sensorTypeRepository.save(sensorType);
    }

    @Override
    public Optional<SensorType> partialUpdate(SensorType sensorType) {
        log.debug("Request to partially update SensorType : {}", sensorType);

        return sensorTypeRepository
            .findById(sensorType.getId())
            .map(existingSensorType -> {
                if (sensorType.getCode() != null) {
                    existingSensorType.setCode(sensorType.getCode());
                }
                if (sensorType.getName() != null) {
                    existingSensorType.setName(sensorType.getName());
                }
                if (sensorType.getDescription() != null) {
                    existingSensorType.setDescription(sensorType.getDescription());
                }

                return existingSensorType;
            })
            .map(sensorTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorType> findAll() {
        log.debug("Request to get all SensorTypes");
        return sensorTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SensorType> findOne(Long id) {
        log.debug("Request to get SensorType : {}", id);
        return sensorTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorType : {}", id);
        sensorTypeRepository.deleteById(id);
    }
}
