package com.herve.app.service;

import com.herve.app.domain.SensorData;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SensorData}.
 */
public interface SensorDataService {
    /**
     * Save a sensorData.
     *
     * @param sensorData the entity to save.
     * @return the persisted entity.
     */
    SensorData save(SensorData sensorData);

    /**
     * Updates a sensorData.
     *
     * @param sensorData the entity to update.
     * @return the persisted entity.
     */
    SensorData update(SensorData sensorData);

    /**
     * Partially updates a sensorData.
     *
     * @param sensorData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SensorData> partialUpdate(SensorData sensorData);

    /**
     * Get all the sensorData.
     *
     * @return the list of entities.
     */
    List<SensorData> findAll();

    /**
     * Get the "id" sensorData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SensorData> findOne(Long id);

    /**
     * Delete the "id" sensorData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
