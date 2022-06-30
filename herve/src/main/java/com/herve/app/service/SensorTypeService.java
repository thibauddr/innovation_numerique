package com.herve.app.service;

import com.herve.app.domain.SensorType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SensorType}.
 */
public interface SensorTypeService {
    /**
     * Save a sensorType.
     *
     * @param sensorType the entity to save.
     * @return the persisted entity.
     */
    SensorType save(SensorType sensorType);

    /**
     * Updates a sensorType.
     *
     * @param sensorType the entity to update.
     * @return the persisted entity.
     */
    SensorType update(SensorType sensorType);

    /**
     * Partially updates a sensorType.
     *
     * @param sensorType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SensorType> partialUpdate(SensorType sensorType);

    /**
     * Get all the sensorTypes.
     *
     * @return the list of entities.
     */
    List<SensorType> findAll();

    /**
     * Get the "id" sensorType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SensorType> findOne(Long id);

    /**
     * Delete the "id" sensorType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
