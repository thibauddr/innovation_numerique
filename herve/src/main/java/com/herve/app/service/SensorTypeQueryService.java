package com.herve.app.service;

import com.herve.app.domain.*; // for static metamodels
import com.herve.app.domain.SensorType;
import com.herve.app.repository.SensorTypeRepository;
import com.herve.app.service.criteria.SensorTypeCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SensorType} entities in the database.
 * The main input is a {@link SensorTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SensorType} or a {@link Page} of {@link SensorType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SensorTypeQueryService extends QueryService<SensorType> {

    private final Logger log = LoggerFactory.getLogger(SensorTypeQueryService.class);

    private final SensorTypeRepository sensorTypeRepository;

    public SensorTypeQueryService(SensorTypeRepository sensorTypeRepository) {
        this.sensorTypeRepository = sensorTypeRepository;
    }

    /**
     * Return a {@link List} of {@link SensorType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SensorType> findByCriteria(SensorTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SensorType> specification = createSpecification(criteria);
        return sensorTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SensorType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SensorType> findByCriteria(SensorTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SensorType> specification = createSpecification(criteria);
        return sensorTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SensorTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SensorType> specification = createSpecification(criteria);
        return sensorTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link SensorTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SensorType> createSpecification(SensorTypeCriteria criteria) {
        Specification<SensorType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SensorType_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), SensorType_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SensorType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SensorType_.description));
            }
        }
        return specification;
    }
}
