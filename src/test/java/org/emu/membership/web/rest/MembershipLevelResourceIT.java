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
import org.emu.membership.domain.MembershipLevel;
import org.emu.membership.repository.MembershipLevelRepository;
import org.emu.membership.repository.search.MembershipLevelSearchRepository;
import org.emu.membership.service.dto.MembershipLevelDTO;
import org.emu.membership.service.mapper.MembershipLevelMapper;
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
 * Integration tests for the {@link MembershipLevelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MembershipLevelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MembershipLevelRepository membershipLevelRepository;

    @Autowired
    private MembershipLevelMapper membershipLevelMapper;

    /**
     * This repository is mocked in the org.emu.membership.repository.search test package.
     *
     * @see org.emu.membership.repository.search.MembershipLevelSearchRepositoryMockConfiguration
     */
    @Autowired
    private MembershipLevelSearchRepository mockMembershipLevelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipLevelMockMvc;

    private MembershipLevel membershipLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipLevel createEntity(EntityManager em) {
        MembershipLevel membershipLevel = new MembershipLevel().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return membershipLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipLevel createUpdatedEntity(EntityManager em) {
        MembershipLevel membershipLevel = new MembershipLevel().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return membershipLevel;
    }

    @BeforeEach
    public void initTest() {
        membershipLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createMembershipLevel() throws Exception {
        int databaseSizeBeforeCreate = membershipLevelRepository.findAll().size();
        // Create the MembershipLevel
        MembershipLevelDTO membershipLevelDTO = membershipLevelMapper.toDto(membershipLevel);
        restMembershipLevelMockMvc
            .perform(
                post("/api/membership-levels")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembershipLevel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the MembershipLevel in Elasticsearch
        verify(mockMembershipLevelSearchRepository, times(1)).save(testMembershipLevel);
    }

    @Test
    @Transactional
    void createMembershipLevelWithExistingId() throws Exception {
        // Create the MembershipLevel with an existing ID
        membershipLevel.setId(1L);
        MembershipLevelDTO membershipLevelDTO = membershipLevelMapper.toDto(membershipLevel);

        int databaseSizeBeforeCreate = membershipLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipLevelMockMvc
            .perform(
                post("/api/membership-levels")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeCreate);

        // Validate the MembershipLevel in Elasticsearch
        verify(mockMembershipLevelSearchRepository, times(0)).save(membershipLevel);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipLevelRepository.findAll().size();
        // set the field null
        membershipLevel.setName(null);

        // Create the MembershipLevel, which fails.
        MembershipLevelDTO membershipLevelDTO = membershipLevelMapper.toDto(membershipLevel);

        restMembershipLevelMockMvc
            .perform(
                post("/api/membership-levels")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevelDTO))
            )
            .andExpect(status().isBadRequest());

        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembershipLevels() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList
        restMembershipLevelMockMvc
            .perform(get("/api/membership-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMembershipLevel() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get the membershipLevel
        restMembershipLevelMockMvc
            .perform(get("/api/membership-levels/{id}", membershipLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipLevel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMembershipLevel() throws Exception {
        // Get the membershipLevel
        restMembershipLevelMockMvc.perform(get("/api/membership-levels/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMembershipLevel() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();

        // Update the membershipLevel
        MembershipLevel updatedMembershipLevel = membershipLevelRepository.findById(membershipLevel.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipLevel are not directly saved in db
        em.detach(updatedMembershipLevel);
        updatedMembershipLevel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        MembershipLevelDTO membershipLevelDTO = membershipLevelMapper.toDto(updatedMembershipLevel);

        restMembershipLevelMockMvc
            .perform(
                put("/api/membership-levels")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevelDTO))
            )
            .andExpect(status().isOk());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the MembershipLevel in Elasticsearch
        verify(mockMembershipLevelSearchRepository).save(testMembershipLevel);
    }

    @Test
    @Transactional
    void updateNonExistingMembershipLevel() throws Exception {
        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();

        // Create the MembershipLevel
        MembershipLevelDTO membershipLevelDTO = membershipLevelMapper.toDto(membershipLevel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipLevelMockMvc
            .perform(
                put("/api/membership-levels")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MembershipLevel in Elasticsearch
        verify(mockMembershipLevelSearchRepository, times(0)).save(membershipLevel);
    }

    @Test
    @Transactional
    void partialUpdateMembershipLevelWithPatch() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();

        // Update the membershipLevel using partial update
        MembershipLevel partialUpdatedMembershipLevel = new MembershipLevel();
        partialUpdatedMembershipLevel.setId(membershipLevel.getId());

        partialUpdatedMembershipLevel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMembershipLevelMockMvc
            .perform(
                patch("/api/membership-levels")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipLevel))
            )
            .andExpect(status().isOk());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMembershipLevelWithPatch() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();

        // Update the membershipLevel using partial update
        MembershipLevel partialUpdatedMembershipLevel = new MembershipLevel();
        partialUpdatedMembershipLevel.setId(membershipLevel.getId());

        partialUpdatedMembershipLevel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMembershipLevelMockMvc
            .perform(
                patch("/api/membership-levels")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipLevel))
            )
            .andExpect(status().isOk());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void partialUpdateMembershipLevelShouldThrown() throws Exception {
        // Update the membershipLevel without id should throw
        MembershipLevel partialUpdatedMembershipLevel = new MembershipLevel();

        restMembershipLevelMockMvc
            .perform(
                patch("/api/membership-levels")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipLevel))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMembershipLevel() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeDelete = membershipLevelRepository.findAll().size();

        // Delete the membershipLevel
        restMembershipLevelMockMvc
            .perform(delete("/api/membership-levels/{id}", membershipLevel.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MembershipLevel in Elasticsearch
        verify(mockMembershipLevelSearchRepository, times(1)).deleteById(membershipLevel.getId());
    }

    @Test
    @Transactional
    void searchMembershipLevel() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);
        when(mockMembershipLevelSearchRepository.search(queryStringQuery("id:" + membershipLevel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(membershipLevel), PageRequest.of(0, 1), 1));

        // Search the membershipLevel
        restMembershipLevelMockMvc
            .perform(get("/api/_search/membership-levels?query=id:" + membershipLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
