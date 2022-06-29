package com.herve.app.web.rest;

import com.herve.app.domain.Field;
import com.herve.app.repository.FieldRepository;
import com.herve.app.service.FieldQueryService;
import com.herve.app.service.FieldService;
import com.herve.app.service.criteria.FieldCriteria;
import com.herve.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.herve.app.domain.Field}.
 */
@RestController
@RequestMapping("/api")
public class FieldResource {

    private final Logger log = LoggerFactory.getLogger(FieldResource.class);

    private static final String ENTITY_NAME = "field";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FieldService fieldService;

    private final FieldRepository fieldRepository;

    private final FieldQueryService fieldQueryService;

    public FieldResource(FieldService fieldService, FieldRepository fieldRepository, FieldQueryService fieldQueryService) {
        this.fieldService = fieldService;
        this.fieldRepository = fieldRepository;
        this.fieldQueryService = fieldQueryService;
    }

    /**
     * {@code POST  /fields} : Create a new field.
     *
     * @param field the field to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new field, or with status {@code 400 (Bad Request)} if the field has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fields")
    public ResponseEntity<Field> createField(@RequestBody Field field) throws URISyntaxException {
        log.debug("REST request to save Field : {}", field);
        if (field.getId() != null) {
            throw new BadRequestAlertException("A new field cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Field result = fieldService.save(field);
        return ResponseEntity
            .created(new URI("/api/fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fields/:id} : Updates an existing field.
     *
     * @param id the id of the field to save.
     * @param field the field to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated field,
     * or with status {@code 400 (Bad Request)} if the field is not valid,
     * or with status {@code 500 (Internal Server Error)} if the field couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fields/{id}")
    public ResponseEntity<Field> updateField(@PathVariable(value = "id", required = false) final Long id, @RequestBody Field field)
        throws URISyntaxException {
        log.debug("REST request to update Field : {}, {}", id, field);
        if (field.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, field.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Field result = fieldService.update(field);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, field.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fields/:id} : Partial updates given fields of an existing field, field will ignore if it is null
     *
     * @param id the id of the field to save.
     * @param field the field to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated field,
     * or with status {@code 400 (Bad Request)} if the field is not valid,
     * or with status {@code 404 (Not Found)} if the field is not found,
     * or with status {@code 500 (Internal Server Error)} if the field couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Field> partialUpdateField(@PathVariable(value = "id", required = false) final Long id, @RequestBody Field field)
        throws URISyntaxException {
        log.debug("REST request to partial update Field partially : {}, {}", id, field);
        if (field.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, field.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Field> result = fieldService.partialUpdate(field);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, field.getId().toString())
        );
    }

    /**
     * {@code GET  /fields} : get all the fields.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fields in body.
     */
    @GetMapping("/fields")
    public ResponseEntity<List<Field>> getAllFields(FieldCriteria criteria) {
        log.debug("REST request to get Fields by criteria: {}", criteria);
        List<Field> entityList = fieldQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /fields/count} : count all the fields.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fields/count")
    public ResponseEntity<Long> countFields(FieldCriteria criteria) {
        log.debug("REST request to count Fields by criteria: {}", criteria);
        return ResponseEntity.ok().body(fieldQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fields/:id} : get the "id" field.
     *
     * @param id the id of the field to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the field, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fields/{id}")
    public ResponseEntity<Field> getField(@PathVariable Long id) {
        log.debug("REST request to get Field : {}", id);
        Optional<Field> field = fieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(field);
    }

    /**
     * {@code DELETE  /fields/:id} : delete the "id" field.
     *
     * @param id the id of the field to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fields/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        log.debug("REST request to delete Field : {}", id);
        fieldService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
