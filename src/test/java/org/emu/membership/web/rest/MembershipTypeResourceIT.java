package org.emu.membership.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.emu.membership.IntegrationTest;
import org.emu.membership.domain.MembershipType;
import org.emu.membership.repository.MembershipTypeRepository;
import org.emu.membership.repository.search.MembershipTypeSearchRepository;
import org.emu.membership.service.dto.MembershipTypeDTO;
import org.emu.membership.service.mapper.MembershipTypeMapper;
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
 * Integration tests for the {@link MembershipTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MembershipTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MembershipTypeRepository membershipTypeRepository;

    @Autowired
    private MembershipTypeMapper membershipTypeMapper;

    /**
     * This repository is mocked in the org.emu.membership.repository.search test package.
     *
     * @see org.emu.membership.repository.search.MembershipTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private MembershipTypeSearchRepository mockMembershipTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipTypeMockMvc;

    private MembershipType membershipType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipType createEntity(EntityManager em) {
        MembershipType membershipType = new MembershipType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return membershipType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipType createUpdatedEntity(EntityManager em) {
        MembershipType membershipType = new MembershipType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return membershipType;
    }

    @BeforeEach
    public void initTest() {
        membershipType = createEntity(em);
    }

    @Test
    @Transactional
    void createMembershipType() throws Exception {
        int databaseSizeBeforeCreate = membershipTypeRepository.findAll().size();
        // Create the MembershipType
        MembershipTypeDTO membershipTypeDTO = membershipTypeMapper.toDto(membershipType);
        restMembershipTypeMockMvc
            .perform(
                post("/api/membership-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MembershipType in the database
        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipType testMembershipType = membershipTypeList.get(membershipTypeList.size() - 1);
        assertThat(testMembershipType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembershipType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the MembershipType in Elasticsearch
        verify(mockMembershipTypeSearchRepository, times(1)).save(testMembershipType);
    }

    @Test
    @Transactional
    void createMembershipTypeWithExistingId() throws Exception {
        // Create the MembershipType with an existing ID
        membershipType.setId(1L);
        MembershipTypeDTO membershipTypeDTO = membershipTypeMapper.toDto(membershipType);

        int databaseSizeBeforeCreate = membershipTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipTypeMockMvc
            .perform(
                post("/api/membership-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipType in the database
        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the MembershipType in Elasticsearch
        verify(mockMembershipTypeSearchRepository, times(0)).save(membershipType);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipTypeRepository.findAll().size();
        // set the field null
        membershipType.setName(null);

        // Create the MembershipType, which fails.
        MembershipTypeDTO membershipTypeDTO = membershipTypeMapper.toDto(membershipType);

        restMembershipTypeMockMvc
            .perform(
                post("/api/membership-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembershipTypes() throws Exception {
        // Initialize the database
        membershipTypeRepository.saveAndFlush(membershipType);

        // Get all the membershipTypeList
        restMembershipTypeMockMvc
            .perform(get("/api/membership-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMembershipType() throws Exception {
        // Initialize the database
        membershipTypeRepository.saveAndFlush(membershipType);

        // Get the membershipType
        restMembershipTypeMockMvc
            .perform(get("/api/membership-types/{id}", membershipType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMembershipType() throws Exception {
        // Get the membershipType
        restMembershipTypeMockMvc.perform(get("/api/membership-types/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMembershipType() throws Exception {
        // Initialize the database
        membershipTypeRepository.saveAndFlush(membershipType);

        int databaseSizeBeforeUpdate = membershipTypeRepository.findAll().size();

        // Update the membershipType
        MembershipType updatedMembershipType = membershipTypeRepository.findById(membershipType.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipType are not directly saved in db
        em.detach(updatedMembershipType);
        updatedMembershipType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        MembershipTypeDTO membershipTypeDTO = membershipTypeMapper.toDto(updatedMembershipType);

        restMembershipTypeMockMvc
            .perform(
                put("/api/membership-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MembershipType in the database
        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeUpdate);
        MembershipType testMembershipType = membershipTypeList.get(membershipTypeList.size() - 1);
        assertThat(testMembershipType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the MembershipType in Elasticsearch
        verify(mockMembershipTypeSearchRepository).save(testMembershipType);
    }

    @Test
    @Transactional
    void updateNonExistingMembershipType() throws Exception {
        int databaseSizeBeforeUpdate = membershipTypeRepository.findAll().size();

        // Create the MembershipType
        MembershipTypeDTO membershipTypeDTO = membershipTypeMapper.toDto(membershipType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipTypeMockMvc
            .perform(
                put("/api/membership-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipType in the database
        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MembershipType in Elasticsearch
        verify(mockMembershipTypeSearchRepository, times(0)).save(membershipType);
    }

    @Test
    @Transactional
    void partialUpdateMembershipTypeWithPatch() throws Exception {
        // Initialize the database
        membershipTypeRepository.saveAndFlush(membershipType);

        int databaseSizeBeforeUpdate = membershipTypeRepository.findAll().size();

        // Update the membershipType using partial update
        MembershipType partialUpdatedMembershipType = new MembershipType();
        partialUpdatedMembershipType.setId(membershipType.getId());

        partialUpdatedMembershipType.description(UPDATED_DESCRIPTION);

        restMembershipTypeMockMvc
            .perform(
                patch("/api/membership-types")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipType))
            )
            .andExpect(status().isOk());

        // Validate the MembershipType in the database
        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeUpdate);
        MembershipType testMembershipType = membershipTypeList.get(membershipTypeList.size() - 1);
        assertThat(testMembershipType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembershipType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMembershipTypeWithPatch() throws Exception {
        // Initialize the database
        membershipTypeRepository.saveAndFlush(membershipType);

        int databaseSizeBeforeUpdate = membershipTypeRepository.findAll().size();

        // Update the membershipType using partial update
        MembershipType partialUpdatedMembershipType = new MembershipType();
        partialUpdatedMembershipType.setId(membershipType.getId());

        partialUpdatedMembershipType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMembershipTypeMockMvc
            .perform(
                patch("/api/membership-types")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipType))
            )
            .andExpect(status().isOk());

        // Validate the MembershipType in the database
        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeUpdate);
        MembershipType testMembershipType = membershipTypeList.get(membershipTypeList.size() - 1);
        assertThat(testMembershipType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void partialUpdateMembershipTypeShouldThrown() throws Exception {
        // Update the membershipType without id should throw
        MembershipType partialUpdatedMembershipType = new MembershipType();

        restMembershipTypeMockMvc
            .perform(
                patch("/api/membership-types")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipType))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMembershipType() throws Exception {
        // Initialize the database
        membershipTypeRepository.saveAndFlush(membershipType);

        int databaseSizeBeforeDelete = membershipTypeRepository.findAll().size();

        // Delete the membershipType
        restMembershipTypeMockMvc
            .perform(delete("/api/membership-types/{id}", membershipType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipType> membershipTypeList = membershipTypeRepository.findAll();
        assertThat(membershipTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MembershipType in Elasticsearch
        verify(mockMembershipTypeSearchRepository, times(1)).deleteById(membershipType.getId());
    }

    @Test
    @Transactional
    void searchMembershipType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        membershipTypeRepository.saveAndFlush(membershipType);
        when(mockMembershipTypeSearchRepository.search(queryStringQuery("id:" + membershipType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(membershipType), PageRequest.of(0, 1), 1));

        // Search the membershipType
        restMembershipTypeMockMvc
            .perform(get("/api/_search/membership-types?query=id:" + membershipType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
