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
import org.emu.membership.domain.MembershipCategory;
import org.emu.membership.repository.MembershipCategoryRepository;
import org.emu.membership.repository.search.MembershipCategorySearchRepository;
import org.emu.membership.service.dto.MembershipCategoryDTO;
import org.emu.membership.service.mapper.MembershipCategoryMapper;
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
 * Integration tests for the {@link MembershipCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MembershipCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MembershipCategoryRepository membershipCategoryRepository;

    @Autowired
    private MembershipCategoryMapper membershipCategoryMapper;

    /**
     * This repository is mocked in the org.emu.membership.repository.search test package.
     *
     * @see org.emu.membership.repository.search.MembershipCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private MembershipCategorySearchRepository mockMembershipCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipCategoryMockMvc;

    private MembershipCategory membershipCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipCategory createEntity(EntityManager em) {
        MembershipCategory membershipCategory = new MembershipCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return membershipCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipCategory createUpdatedEntity(EntityManager em) {
        MembershipCategory membershipCategory = new MembershipCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return membershipCategory;
    }

    @BeforeEach
    public void initTest() {
        membershipCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createMembershipCategory() throws Exception {
        int databaseSizeBeforeCreate = membershipCategoryRepository.findAll().size();
        // Create the MembershipCategory
        MembershipCategoryDTO membershipCategoryDTO = membershipCategoryMapper.toDto(membershipCategory);
        restMembershipCategoryMockMvc
            .perform(
                post("/api/membership-categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MembershipCategory in the database
        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipCategory testMembershipCategory = membershipCategoryList.get(membershipCategoryList.size() - 1);
        assertThat(testMembershipCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembershipCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the MembershipCategory in Elasticsearch
        verify(mockMembershipCategorySearchRepository, times(1)).save(testMembershipCategory);
    }

    @Test
    @Transactional
    void createMembershipCategoryWithExistingId() throws Exception {
        // Create the MembershipCategory with an existing ID
        membershipCategory.setId(1L);
        MembershipCategoryDTO membershipCategoryDTO = membershipCategoryMapper.toDto(membershipCategory);

        int databaseSizeBeforeCreate = membershipCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipCategoryMockMvc
            .perform(
                post("/api/membership-categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipCategory in the database
        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the MembershipCategory in Elasticsearch
        verify(mockMembershipCategorySearchRepository, times(0)).save(membershipCategory);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipCategoryRepository.findAll().size();
        // set the field null
        membershipCategory.setName(null);

        // Create the MembershipCategory, which fails.
        MembershipCategoryDTO membershipCategoryDTO = membershipCategoryMapper.toDto(membershipCategory);

        restMembershipCategoryMockMvc
            .perform(
                post("/api/membership-categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembershipCategories() throws Exception {
        // Initialize the database
        membershipCategoryRepository.saveAndFlush(membershipCategory);

        // Get all the membershipCategoryList
        restMembershipCategoryMockMvc
            .perform(get("/api/membership-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMembershipCategory() throws Exception {
        // Initialize the database
        membershipCategoryRepository.saveAndFlush(membershipCategory);

        // Get the membershipCategory
        restMembershipCategoryMockMvc
            .perform(get("/api/membership-categories/{id}", membershipCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMembershipCategory() throws Exception {
        // Get the membershipCategory
        restMembershipCategoryMockMvc.perform(get("/api/membership-categories/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMembershipCategory() throws Exception {
        // Initialize the database
        membershipCategoryRepository.saveAndFlush(membershipCategory);

        int databaseSizeBeforeUpdate = membershipCategoryRepository.findAll().size();

        // Update the membershipCategory
        MembershipCategory updatedMembershipCategory = membershipCategoryRepository.findById(membershipCategory.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipCategory are not directly saved in db
        em.detach(updatedMembershipCategory);
        updatedMembershipCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        MembershipCategoryDTO membershipCategoryDTO = membershipCategoryMapper.toDto(updatedMembershipCategory);

        restMembershipCategoryMockMvc
            .perform(
                put("/api/membership-categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MembershipCategory in the database
        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeUpdate);
        MembershipCategory testMembershipCategory = membershipCategoryList.get(membershipCategoryList.size() - 1);
        assertThat(testMembershipCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the MembershipCategory in Elasticsearch
        verify(mockMembershipCategorySearchRepository).save(testMembershipCategory);
    }

    @Test
    @Transactional
    void updateNonExistingMembershipCategory() throws Exception {
        int databaseSizeBeforeUpdate = membershipCategoryRepository.findAll().size();

        // Create the MembershipCategory
        MembershipCategoryDTO membershipCategoryDTO = membershipCategoryMapper.toDto(membershipCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipCategoryMockMvc
            .perform(
                put("/api/membership-categories")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipCategory in the database
        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MembershipCategory in Elasticsearch
        verify(mockMembershipCategorySearchRepository, times(0)).save(membershipCategory);
    }

    @Test
    @Transactional
    void partialUpdateMembershipCategoryWithPatch() throws Exception {
        // Initialize the database
        membershipCategoryRepository.saveAndFlush(membershipCategory);

        int databaseSizeBeforeUpdate = membershipCategoryRepository.findAll().size();

        // Update the membershipCategory using partial update
        MembershipCategory partialUpdatedMembershipCategory = new MembershipCategory();
        partialUpdatedMembershipCategory.setId(membershipCategory.getId());

        partialUpdatedMembershipCategory.description(UPDATED_DESCRIPTION);

        restMembershipCategoryMockMvc
            .perform(
                patch("/api/membership-categories")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipCategory))
            )
            .andExpect(status().isOk());

        // Validate the MembershipCategory in the database
        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeUpdate);
        MembershipCategory testMembershipCategory = membershipCategoryList.get(membershipCategoryList.size() - 1);
        assertThat(testMembershipCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembershipCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMembershipCategoryWithPatch() throws Exception {
        // Initialize the database
        membershipCategoryRepository.saveAndFlush(membershipCategory);

        int databaseSizeBeforeUpdate = membershipCategoryRepository.findAll().size();

        // Update the membershipCategory using partial update
        MembershipCategory partialUpdatedMembershipCategory = new MembershipCategory();
        partialUpdatedMembershipCategory.setId(membershipCategory.getId());

        partialUpdatedMembershipCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMembershipCategoryMockMvc
            .perform(
                patch("/api/membership-categories")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipCategory))
            )
            .andExpect(status().isOk());

        // Validate the MembershipCategory in the database
        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeUpdate);
        MembershipCategory testMembershipCategory = membershipCategoryList.get(membershipCategoryList.size() - 1);
        assertThat(testMembershipCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembershipCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void partialUpdateMembershipCategoryShouldThrown() throws Exception {
        // Update the membershipCategory without id should throw
        MembershipCategory partialUpdatedMembershipCategory = new MembershipCategory();

        restMembershipCategoryMockMvc
            .perform(
                patch("/api/membership-categories")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipCategory))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMembershipCategory() throws Exception {
        // Initialize the database
        membershipCategoryRepository.saveAndFlush(membershipCategory);

        int databaseSizeBeforeDelete = membershipCategoryRepository.findAll().size();

        // Delete the membershipCategory
        restMembershipCategoryMockMvc
            .perform(delete("/api/membership-categories/{id}", membershipCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipCategory> membershipCategoryList = membershipCategoryRepository.findAll();
        assertThat(membershipCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MembershipCategory in Elasticsearch
        verify(mockMembershipCategorySearchRepository, times(1)).deleteById(membershipCategory.getId());
    }

    @Test
    @Transactional
    void searchMembershipCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        membershipCategoryRepository.saveAndFlush(membershipCategory);
        when(mockMembershipCategorySearchRepository.search(queryStringQuery("id:" + membershipCategory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(membershipCategory), PageRequest.of(0, 1), 1));

        // Search the membershipCategory
        restMembershipCategoryMockMvc
            .perform(get("/api/_search/membership-categories?query=id:" + membershipCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
