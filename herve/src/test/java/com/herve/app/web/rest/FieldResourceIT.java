package com.herve.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.herve.app.IntegrationTest;
import com.herve.app.domain.Field;
import com.herve.app.domain.Sensor;
import com.herve.app.domain.User;
import com.herve.app.repository.FieldRepository;
import com.herve.app.service.FieldService;
import com.herve.app.service.criteria.FieldCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FieldResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FieldResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FieldRepository fieldRepository;

    @Mock
    private FieldRepository fieldRepositoryMock;

    @Mock
    private FieldService fieldServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFieldMockMvc;

    private Field field;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createEntity(EntityManager em) {
        Field field = new Field().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).position(DEFAULT_POSITION);
        return field;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createUpdatedEntity(EntityManager em) {
        Field field = new Field().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).position(UPDATED_POSITION);
        return field;
    }

    @BeforeEach
    public void initTest() {
        field = createEntity(em);
    }

    @Test
    @Transactional
    void createField() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();
        // Create the Field
        restFieldMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testField.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testField.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void createFieldWithExistingId() throws Exception {
        // Create the Field with an existing ID
        field.setId(1L);

        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFields() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList
        restFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFieldsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fieldServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fieldServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFieldsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fieldServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fieldServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get the field
        restFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(field.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getFieldsByIdFiltering() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        Long id = field.getId();

        defaultFieldShouldBeFound("id.equals=" + id);
        defaultFieldShouldNotBeFound("id.notEquals=" + id);

        defaultFieldShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFieldShouldNotBeFound("id.greaterThan=" + id);

        defaultFieldShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFieldShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFieldsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where name equals to DEFAULT_NAME
        defaultFieldShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the fieldList where name equals to UPDATED_NAME
        defaultFieldShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where name not equals to DEFAULT_NAME
        defaultFieldShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the fieldList where name not equals to UPDATED_NAME
        defaultFieldShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFieldShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the fieldList where name equals to UPDATED_NAME
        defaultFieldShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where name is not null
        defaultFieldShouldBeFound("name.specified=true");

        // Get all the fieldList where name is null
        defaultFieldShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFieldsByNameContainsSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where name contains DEFAULT_NAME
        defaultFieldShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the fieldList where name contains UPDATED_NAME
        defaultFieldShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where name does not contain DEFAULT_NAME
        defaultFieldShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the fieldList where name does not contain UPDATED_NAME
        defaultFieldShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFieldsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where description equals to DEFAULT_DESCRIPTION
        defaultFieldShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fieldList where description equals to UPDATED_DESCRIPTION
        defaultFieldShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFieldsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where description not equals to DEFAULT_DESCRIPTION
        defaultFieldShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the fieldList where description not equals to UPDATED_DESCRIPTION
        defaultFieldShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFieldsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFieldShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fieldList where description equals to UPDATED_DESCRIPTION
        defaultFieldShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFieldsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where description is not null
        defaultFieldShouldBeFound("description.specified=true");

        // Get all the fieldList where description is null
        defaultFieldShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFieldsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where description contains DEFAULT_DESCRIPTION
        defaultFieldShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fieldList where description contains UPDATED_DESCRIPTION
        defaultFieldShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFieldsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where description does not contain DEFAULT_DESCRIPTION
        defaultFieldShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fieldList where description does not contain UPDATED_DESCRIPTION
        defaultFieldShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFieldsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where position equals to DEFAULT_POSITION
        defaultFieldShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the fieldList where position equals to UPDATED_POSITION
        defaultFieldShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllFieldsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where position not equals to DEFAULT_POSITION
        defaultFieldShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the fieldList where position not equals to UPDATED_POSITION
        defaultFieldShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllFieldsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultFieldShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the fieldList where position equals to UPDATED_POSITION
        defaultFieldShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllFieldsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where position is not null
        defaultFieldShouldBeFound("position.specified=true");

        // Get all the fieldList where position is null
        defaultFieldShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllFieldsByPositionContainsSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where position contains DEFAULT_POSITION
        defaultFieldShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the fieldList where position contains UPDATED_POSITION
        defaultFieldShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllFieldsByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList where position does not contain DEFAULT_POSITION
        defaultFieldShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the fieldList where position does not contain UPDATED_POSITION
        defaultFieldShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllFieldsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        field.setUser(user);
        fieldRepository.saveAndFlush(field);
        Long userId = user.getId();

        // Get all the fieldList where user equals to userId
        defaultFieldShouldBeFound("userId.equals=" + userId);

        // Get all the fieldList where user equals to (userId + 1)
        defaultFieldShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllFieldsBySensorIsEqualToSomething() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);
        Sensor sensor;
        if (TestUtil.findAll(em, Sensor.class).isEmpty()) {
            sensor = SensorResourceIT.createEntity(em);
            em.persist(sensor);
            em.flush();
        } else {
            sensor = TestUtil.findAll(em, Sensor.class).get(0);
        }
        em.persist(sensor);
        em.flush();
        field.addSensor(sensor);
        fieldRepository.saveAndFlush(field);
        Long sensorId = sensor.getId();

        // Get all the fieldList where sensor equals to sensorId
        defaultFieldShouldBeFound("sensorId.equals=" + sensorId);

        // Get all the fieldList where sensor equals to (sensorId + 1)
        defaultFieldShouldNotBeFound("sensorId.equals=" + (sensorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFieldShouldBeFound(String filter) throws Exception {
        restFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFieldShouldNotBeFound(String filter) throws Exception {
        restFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingField() throws Exception {
        // Get the field
        restFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field
        Field updatedField = fieldRepository.findById(field.getId()).get();
        // Disconnect from session so that the updates on updatedField are not directly saved in db
        em.detach(updatedField);
        updatedField.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).position(UPDATED_POSITION);

        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedField.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testField.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testField.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, field.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFieldWithPatch() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field using partial update
        Field partialUpdatedField = new Field();
        partialUpdatedField.setId(field.getId());

        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedField.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testField.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testField.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateFieldWithPatch() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field using partial update
        Field partialUpdatedField = new Field();
        partialUpdatedField.setId(field.getId());

        partialUpdatedField.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).position(UPDATED_POSITION);

        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedField.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testField.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testField.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, field.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeDelete = fieldRepository.findAll().size();

        // Delete the field
        restFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, field.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
