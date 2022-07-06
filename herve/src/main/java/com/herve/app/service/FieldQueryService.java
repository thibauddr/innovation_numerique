package com.herve.app.service;

import com.herve.app.domain.*; // for static metamodels
import com.herve.app.domain.Field;
import com.herve.app.repository.FieldRepository;
import com.herve.app.service.criteria.FieldCriteria;
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
 * Service for executing complex queries for {@link Field} entities in the database.
 * The main input is a {@link FieldCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Field} or a {@link Page} of {@link Field} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FieldQueryService extends QueryService<Field> {

    private final Logger log = LoggerFactory.getLogger(FieldQueryService.class);

    private final FieldRepository fieldRepository;

    public FieldQueryService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    /**
     * Return a {@link List} of {@link Field} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Field> findByCriteria(FieldCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Field> specification = createSpecification(criteria);
        return fieldRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Field} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Field> findByCriteria(FieldCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Field> specification = createSpecification(criteria);
        return fieldRepository.findAll(specification, page);
    }

    /**
     * Return a {@link List} of {@link Field} which matches the criteria from the database.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Field> findFieldOfConnectedUser() {
        log.debug("find by user connected : {}");
        return fieldRepository.findByUserIsCurrentUser();
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FieldCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Field> specification = createSpecification(criteria);
        return fieldRepository.count(specification);
    }

    /**
     * Function to convert {@link FieldCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Field> createSpecification(FieldCriteria criteria) {
        Specification<Field> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Field_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Field_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Field_.description));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), Field_.position));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Field_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getSensorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSensorId(), root -> root.join(Field_.sensors, JoinType.LEFT).get(Sensor_.id))
                    );
            }
        }
        return specification;
    }
}
