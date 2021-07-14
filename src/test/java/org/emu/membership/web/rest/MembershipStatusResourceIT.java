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
import org.emu.membership.domain.MembershipStatus;
import org.emu.membership.repository.MembershipStatusRepository;
import org.emu.membership.repository.search.MembershipStatusSearchRepository;
import org.emu.membership.service.dto.MembershipStatusDTO;
import org.emu.membership.service.mapper.MembershipStatusMapper;
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
 * Integration tests for the {@link MembershipStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MembershipStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MembershipStatusRepository membershipStatusRepository;

    @Autowired
    private MembershipStatusMapper membershipStatusMapper;

    /**
     * This repository is mocked in the org.emu.membership.repository.search test package.
     *
     * @see org.emu.membership.repository.search.MembershipStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private MembershipStatusSearchRepository mockMembershipStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipStatusMockMvc;

    private MembershipStatus membershipStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipStatus createEntity(EntityManager em) {
        MembershipStatus membershipStatus = new MembershipStatus().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return membershipStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipStatus createUpdatedEntity(EntityManager em) {
        MembershipStatus membershipStatus = new MembershipStatus().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return membershipStatus;
    }

    @BeforeEach
    public void initTest() {
        membershipStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createMembershipStatus() throws Exception {
        int databaseSizeBeforeCreate = membershipStatusRepository.findAll().size();
        // Create the MembershipStatus
        MembershipStatusDTO membershipStatusDTO = membershipStatusMapper.toDto(membershipStatus);
        restMembershipStatusMockMvc
            .perform(
                post("/api/membership-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MembershipStatus in the database
        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipStatus testMembershipStatus = membershipStatusList.get(membershipStatusList.size() - 1);
        assertThat(testMembershipStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembershipStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the MembershipStatus in Elasticsearch
        verify(mockMembershipStatusSearchRepository, times(1)).save(testMembershipStatus);
    }

    @Test
    @Transactional
    void createMembershipStatusWithExistingId() throws Exception {
        // Create the MembershipStatus with an existing ID
        membershipStatus.setId(1L);
        MembershipStatusDTO membershipStatusDTO = membershipStatusMapper.toDto(membershipStatus);

        int databaseSizeBeforeCreate = membershipStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipStatusMockMvc
            .perform(
                post("/api/membership-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipStatus in the database
        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the MembershipStatus in Elasticsearch
        verify(mockMembershipStatusSearchRepository, times(0)).save(membershipStatus);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipStatusRepository.findAll().size();
        // set the field null
        membershipStatus.setName(null);

        // Create the MembershipStatus, which fails.
        MembershipStatusDTO membershipStatusDTO = membershipStatusMapper.toDto(membershipStatus);

        restMembershipStatusMockMvc
            .perform(
                post("/api/membership-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembershipStatuses() throws Exception {
        // Initialize the database
        membershipStatusRepository.saveAndFlush(membershipStatus);

        // Get all the membershipStatusList
        restMembershipStatusMockMvc
            .perform(get("/api/membership-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMembershipStatus() throws Exception {
        // Initialize the database
        membershipStatusRepository.saveAndFlush(membershipStatus);

        // Get the membershipStatus
        restMembershipStatusMockMvc
            .perform(get("/api/membership-statuses/{id}", membershipStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMembershipStatus() throws Exception {
        // Get the membershipStatus
        restMembershipStatusMockMvc.perform(get("/api/membership-statuses/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMembershipStatus() throws Exception {
        // Initialize the database
        membershipStatusRepository.saveAndFlush(membershipStatus);

        int databaseSizeBeforeUpdate = membershipStatusRepository.findAll().size();

        // Update the membershipStatus
        MembershipStatus updatedMembershipStatus = membershipStatusRepository.findById(membershipStatus.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipStatus are not directly saved in db
        em.detach(updatedMembershipStatus);
        updatedMembershipStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        MembershipStatusDTO membershipStatusDTO = membershipStatusMapper.toDto(updatedMembershipStatus);

        restMembershipStatusMockMvc
            .perform(
                put("/api/membership-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the MembershipStatus in the database
        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeUpdate);
        MembershipStatus testMembershipStatus = membershipStatusList.get(membershipStatusList.size() - 1);
        assertThat(testMembershipStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the MembershipStatus in Elasticsearch
        verify(mockMembershipStatusSearchRepository).save(testMembershipStatus);
    }

    @Test
    @Transactional
    void updateNonExistingMembershipStatus() throws Exception {
        int databaseSizeBeforeUpdate = membershipStatusRepository.findAll().size();

        // Create the MembershipStatus
        MembershipStatusDTO membershipStatusDTO = membershipStatusMapper.toDto(membershipStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipStatusMockMvc
            .perform(
                put("/api/membership-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipStatus in the database
        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MembershipStatus in Elasticsearch
        verify(mockMembershipStatusSearchRepository, times(0)).save(membershipStatus);
    }

    @Test
    @Transactional
    void partialUpdateMembershipStatusWithPatch() throws Exception {
        // Initialize the database
        membershipStatusRepository.saveAndFlush(membershipStatus);

        int databaseSizeBeforeUpdate = membershipStatusRepository.findAll().size();

        // Update the membershipStatus using partial update
        MembershipStatus partialUpdatedMembershipStatus = new MembershipStatus();
        partialUpdatedMembershipStatus.setId(membershipStatus.getId());

        partialUpdatedMembershipStatus.description(UPDATED_DESCRIPTION);

        restMembershipStatusMockMvc
            .perform(
                patch("/api/membership-statuses")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipStatus))
            )
            .andExpect(status().isOk());

        // Validate the MembershipStatus in the database
        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeUpdate);
        MembershipStatus testMembershipStatus = membershipStatusList.get(membershipStatusList.size() - 1);
        assertThat(testMembershipStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembershipStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMembershipStatusWithPatch() throws Exception {
        // Initialize the database
        membershipStatusRepository.saveAndFlush(membershipStatus);

        int databaseSizeBeforeUpdate = membershipStatusRepository.findAll().size();

        // Update the membershipStatus using partial update
        MembershipStatus partialUpdatedMembershipStatus = new MembershipStatus();
        partialUpdatedMembershipStatus.setId(membershipStatus.getId());

        partialUpdatedMembershipStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMembershipStatusMockMvc
            .perform(
                patch("/api/membership-statuses")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipStatus))
            )
            .andExpect(status().isOk());

        // Validate the MembershipStatus in the database
        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeUpdate);
        MembershipStatus testMembershipStatus = membershipStatusList.get(membershipStatusList.size() - 1);
        assertThat(testMembershipStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void partialUpdateMembershipStatusShouldThrown() throws Exception {
        // Update the membershipStatus without id should throw
        MembershipStatus partialUpdatedMembershipStatus = new MembershipStatus();

        restMembershipStatusMockMvc
            .perform(
                patch("/api/membership-statuses")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipStatus))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMembershipStatus() throws Exception {
        // Initialize the database
        membershipStatusRepository.saveAndFlush(membershipStatus);

        int databaseSizeBeforeDelete = membershipStatusRepository.findAll().size();

        // Delete the membershipStatus
        restMembershipStatusMockMvc
            .perform(delete("/api/membership-statuses/{id}", membershipStatus.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipStatus> membershipStatusList = membershipStatusRepository.findAll();
        assertThat(membershipStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MembershipStatus in Elasticsearch
        verify(mockMembershipStatusSearchRepository, times(1)).deleteById(membershipStatus.getId());
    }

    @Test
    @Transactional
    void searchMembershipStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        membershipStatusRepository.saveAndFlush(membershipStatus);
        when(mockMembershipStatusSearchRepository.search(queryStringQuery("id:" + membershipStatus.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(membershipStatus), PageRequest.of(0, 1), 1));

        // Search the membershipStatus
        restMembershipStatusMockMvc
            .perform(get("/api/_search/membership-statuses?query=id:" + membershipStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
