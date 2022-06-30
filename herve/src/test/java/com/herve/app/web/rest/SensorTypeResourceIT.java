package com.herve.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.herve.app.IntegrationTest;
import com.herve.app.domain.SensorType;
import com.herve.app.repository.SensorTypeRepository;
import com.herve.app.service.criteria.SensorTypeCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SensorTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SensorTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sensor-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensorTypeRepository sensorTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSensorTypeMockMvc;

    private SensorType sensorType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensorType createEntity(EntityManager em) {
        SensorType sensorType = new SensorType().code(DEFAULT_CODE).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return sensorType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensorType createUpdatedEntity(EntityManager em) {
        SensorType sensorType = new SensorType().code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return sensorType;
    }

    @BeforeEach
    public void initTest() {
        sensorType = createEntity(em);
    }

    @Test
    @Transactional
    void createSensorType() throws Exception {
        int databaseSizeBeforeCreate = sensorTypeRepository.findAll().size();
        // Create the SensorType
        restSensorTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isCreated());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SensorType testSensorType = sensorTypeList.get(sensorTypeList.size() - 1);
        assertThat(testSensorType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSensorType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSensorType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSensorTypeWithExistingId() throws Exception {
        // Create the SensorType with an existing ID
        sensorType.setId(1L);

        int databaseSizeBeforeCreate = sensorTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSensorTypes() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList
        restSensorTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSensorType() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get the sensorType
        restSensorTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, sensorType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensorType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getSensorTypesByIdFiltering() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        Long id = sensorType.getId();

        defaultSensorTypeShouldBeFound("id.equals=" + id);
        defaultSensorTypeShouldNotBeFound("id.notEquals=" + id);

        defaultSensorTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSensorTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultSensorTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSensorTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSensorTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where code equals to DEFAULT_CODE
        defaultSensorTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the sensorTypeList where code equals to UPDATED_CODE
        defaultSensorTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSensorTypesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where code not equals to DEFAULT_CODE
        defaultSensorTypeShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the sensorTypeList where code not equals to UPDATED_CODE
        defaultSensorTypeShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSensorTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSensorTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the sensorTypeList where code equals to UPDATED_CODE
        defaultSensorTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSensorTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where code is not null
        defaultSensorTypeShouldBeFound("code.specified=true");

        // Get all the sensorTypeList where code is null
        defaultSensorTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where code contains DEFAULT_CODE
        defaultSensorTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the sensorTypeList where code contains UPDATED_CODE
        defaultSensorTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSensorTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where code does not contain DEFAULT_CODE
        defaultSensorTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the sensorTypeList where code does not contain UPDATED_CODE
        defaultSensorTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSensorTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where name equals to DEFAULT_NAME
        defaultSensorTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sensorTypeList where name equals to UPDATED_NAME
        defaultSensorTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where name not equals to DEFAULT_NAME
        defaultSensorTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the sensorTypeList where name not equals to UPDATED_NAME
        defaultSensorTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSensorTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sensorTypeList where name equals to UPDATED_NAME
        defaultSensorTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where name is not null
        defaultSensorTypeShouldBeFound("name.specified=true");

        // Get all the sensorTypeList where name is null
        defaultSensorTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where name contains DEFAULT_NAME
        defaultSensorTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sensorTypeList where name contains UPDATED_NAME
        defaultSensorTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where name does not contain DEFAULT_NAME
        defaultSensorTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sensorTypeList where name does not contain UPDATED_NAME
        defaultSensorTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSensorTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where description equals to DEFAULT_DESCRIPTION
        defaultSensorTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the sensorTypeList where description equals to UPDATED_DESCRIPTION
        defaultSensorTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultSensorTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the sensorTypeList where description not equals to UPDATED_DESCRIPTION
        defaultSensorTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSensorTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the sensorTypeList where description equals to UPDATED_DESCRIPTION
        defaultSensorTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where description is not null
        defaultSensorTypeShouldBeFound("description.specified=true");

        // Get all the sensorTypeList where description is null
        defaultSensorTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSensorTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where description contains DEFAULT_DESCRIPTION
        defaultSensorTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the sensorTypeList where description contains UPDATED_DESCRIPTION
        defaultSensorTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSensorTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        // Get all the sensorTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultSensorTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the sensorTypeList where description does not contain UPDATED_DESCRIPTION
        defaultSensorTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSensorTypeShouldBeFound(String filter) throws Exception {
        restSensorTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restSensorTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSensorTypeShouldNotBeFound(String filter) throws Exception {
        restSensorTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSensorTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSensorType() throws Exception {
        // Get the sensorType
        restSensorTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSensorType() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();

        // Update the sensorType
        SensorType updatedSensorType = sensorTypeRepository.findById(sensorType.getId()).get();
        // Disconnect from session so that the updates on updatedSensorType are not directly saved in db
        em.detach(updatedSensorType);
        updatedSensorType.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSensorTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSensorType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSensorType))
            )
            .andExpect(status().isOk());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
        SensorType testSensorType = sensorTypeList.get(sensorTypeList.size() - 1);
        assertThat(testSensorType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSensorType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSensorType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSensorType() throws Exception {
        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();
        sensorType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensorType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensorType() throws Exception {
        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();
        sensorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensorType() throws Exception {
        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();
        sensorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensorTypeWithPatch() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();

        // Update the sensorType using partial update
        SensorType partialUpdatedSensorType = new SensorType();
        partialUpdatedSensorType.setId(sensorType.getId());

        partialUpdatedSensorType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restSensorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensorType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensorType))
            )
            .andExpect(status().isOk());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
        SensorType testSensorType = sensorTypeList.get(sensorTypeList.size() - 1);
        assertThat(testSensorType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSensorType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSensorType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSensorTypeWithPatch() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();

        // Update the sensorType using partial update
        SensorType partialUpdatedSensorType = new SensorType();
        partialUpdatedSensorType.setId(sensorType.getId());

        partialUpdatedSensorType.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSensorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensorType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensorType))
            )
            .andExpect(status().isOk());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
        SensorType testSensorType = sensorTypeList.get(sensorTypeList.size() - 1);
        assertThat(testSensorType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSensorType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSensorType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSensorType() throws Exception {
        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();
        sensorType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensorType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensorType() throws Exception {
        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();
        sensorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensorType() throws Exception {
        int databaseSizeBeforeUpdate = sensorTypeRepository.findAll().size();
        sensorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensorType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensorType in the database
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensorType() throws Exception {
        // Initialize the database
        sensorTypeRepository.saveAndFlush(sensorType);

        int databaseSizeBeforeDelete = sensorTypeRepository.findAll().size();

        // Delete the sensorType
        restSensorTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensorType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SensorType> sensorTypeList = sensorTypeRepository.findAll();
        assertThat(sensorTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
