package com.herve.app.service;

import com.herve.app.domain.Field;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Field}.
 */
public interface FieldService {
    /**
     * Save a field.
     *
     * @param field the entity to save.
     * @return the persisted entity.
     */
    Field save(Field field);

    /**
     * Updates a field.
     *
     * @param field the entity to update.
     * @return the persisted entity.
     */
    Field update(Field field);

    /**
     * Partially updates a field.
     *
     * @param field the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Field> partialUpdate(Field field);

    /**
     * Get all the fields.
     *
     * @return the list of entities.
     */
    List<Field> findAll();

    /**
     * Get all the fields with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Field> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" field.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Field> findOne(Long id);

    /**
     * Delete the "id" field.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
