package com.herve.app.service;

import com.herve.app.domain.Sensor;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Sensor}.
 */
public interface SensorService {
    /**
     * Save a sensor.
     *
     * @param sensor the entity to save.
     * @return the persisted entity.
     */
    Sensor save(Sensor sensor);

    /**
     * Updates a sensor.
     *
     * @param sensor the entity to update.
     * @return the persisted entity.
     */
    Sensor update(Sensor sensor);

    /**
     * Partially updates a sensor.
     *
     * @param sensor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sensor> partialUpdate(Sensor sensor);

    /**
     * Get all the sensors.
     *
     * @return the list of entities.
     */
    List<Sensor> findAll();

    /**
     * Get the "id" sensor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sensor> findOne(Long id);

    /**
     * Delete the "id" sensor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
