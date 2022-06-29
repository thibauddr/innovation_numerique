package com.herve.app.service;

import com.herve.app.domain.*; // for static metamodels
import com.herve.app.domain.Sensor;
import com.herve.app.repository.SensorRepository;
import com.herve.app.service.criteria.SensorCriteria;
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
 * Service for executing complex queries for {@link Sensor} entities in the database.
 * The main input is a {@link SensorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sensor} or a {@link Page} of {@link Sensor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SensorQueryService extends QueryService<Sensor> {

    private final Logger log = LoggerFactory.getLogger(SensorQueryService.class);

    private final SensorRepository sensorRepository;

    public SensorQueryService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Return a {@link List} of {@link Sensor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sensor> findByCriteria(SensorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sensor> specification = createSpecification(criteria);
        return sensorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sensor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sensor> findByCriteria(SensorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sensor> specification = createSpecification(criteria);
        return sensorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SensorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sensor> specification = createSpecification(criteria);
        return sensorRepository.count(specification);
    }

    /**
     * Function to convert {@link SensorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sensor> createSpecification(SensorCriteria criteria) {
        Specification<Sensor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sensor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Sensor_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Sensor_.description));
            }
            if (criteria.getPosition_x() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition_x(), Sensor_.position_x));
            }
            if (criteria.getPosition_y() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosition_y(), Sensor_.position_y));
            }
            if (criteria.getFieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFieldId(), root -> root.join(Sensor_.field, JoinType.LEFT).get(Field_.id))
                    );
            }
            if (criteria.getSensorTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSensorTypeId(),
                            root -> root.join(Sensor_.sensorType, JoinType.LEFT).get(SensorType_.id)
                        )
                    );
            }
            if (criteria.getSensorDataId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSensorDataId(),
                            root -> root.join(Sensor_.sensorData, JoinType.LEFT).get(SensorData_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
