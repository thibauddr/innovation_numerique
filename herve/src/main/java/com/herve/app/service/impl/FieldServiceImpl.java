package com.herve.app.service.impl;

import com.herve.app.domain.Field;
import com.herve.app.repository.FieldRepository;
import com.herve.app.service.FieldService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Field}.
 */
@Service
@Transactional
public class FieldServiceImpl implements FieldService {

    private final Logger log = LoggerFactory.getLogger(FieldServiceImpl.class);

    private final FieldRepository fieldRepository;

    public FieldServiceImpl(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Override
    public Field save(Field field) {
        log.debug("Request to save Field : {}", field);
        return fieldRepository.save(field);
    }

    @Override
    public Field update(Field field) {
        log.debug("Request to save Field : {}", field);
        return fieldRepository.save(field);
    }

    @Override
    public Optional<Field> partialUpdate(Field field) {
        log.debug("Request to partially update Field : {}", field);

        return fieldRepository
            .findById(field.getId())
            .map(existingField -> {
                if (field.getName() != null) {
                    existingField.setName(field.getName());
                }
                if (field.getDescription() != null) {
                    existingField.setDescription(field.getDescription());
                }
                if (field.getPosition() != null) {
                    existingField.setPosition(field.getPosition());
                }

                return existingField;
            })
            .map(fieldRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Field> findAll() {
        log.debug("Request to get all Fields");
        return fieldRepository.findAllWithEagerRelationships();
    }

    public Page<Field> findAllWithEagerRelationships(Pageable pageable) {
        return fieldRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Field> findOne(Long id) {
        log.debug("Request to get Field : {}", id);
        return fieldRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Field : {}", id);
        fieldRepository.deleteById(id);
    }
}
