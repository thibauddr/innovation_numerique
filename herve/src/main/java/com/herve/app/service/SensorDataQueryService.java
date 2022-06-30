package com.herve.app.service;

import com.herve.app.domain.*; // for static metamodels
import com.herve.app.domain.SensorData;
import com.herve.app.repository.SensorDataRepository;
import com.herve.app.service.criteria.SensorDataCriteria;
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
 * Service for executing complex queries for {@link SensorData} entities in the database.
 * The main input is a {@link SensorDataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SensorData} or a {@link Page} of {@link SensorData} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SensorDataQueryService extends QueryService<SensorData> {

    private final Logger log = LoggerFactory.getLogger(SensorDataQueryService.class);

    private final SensorDataRepository sensorDataRepository;

    public SensorDataQueryService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    /**
     * Return a {@link List} of {@link SensorData} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SensorData> findByCriteria(SensorDataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SensorData> specification = createSpecification(criteria);
        return sensorDataRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SensorData} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SensorData> findByCriteria(SensorDataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SensorData> specification = createSpecification(criteria);
        return sensorDataRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SensorDataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SensorData> specification = createSpecification(criteria);
        return sensorDataRepository.count(specification);
    }

    /**
     * Function to convert {@link SensorDataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SensorData> createSpecification(SensorDataCriteria criteria) {
        Specification<SensorData> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SensorData_.id));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), SensorData_.unit));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), SensorData_.value));
            }
            if (criteria.getDatetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatetime(), SensorData_.datetime));
            }
            if (criteria.getSensorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSensorId(), root -> root.join(SensorData_.sensor, JoinType.LEFT).get(Sensor_.id))
                    );
            }
        }
        return specification;
    }
}
