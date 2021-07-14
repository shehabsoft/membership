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
import org.emu.membership.domain.MemberFiles;
import org.emu.membership.repository.MemberFilesRepository;
import org.emu.membership.repository.search.MemberFilesSearchRepository;
import org.emu.membership.service.dto.MemberFilesDTO;
import org.emu.membership.service.mapper.MemberFilesMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link MemberFilesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MemberFilesResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private MemberFilesRepository memberFilesRepository;

    @Autowired
    private MemberFilesMapper memberFilesMapper;

    /**
     * This repository is mocked in the org.emu.membership.repository.search test package.
     *
     * @see org.emu.membership.repository.search.MemberFilesSearchRepositoryMockConfiguration
     */
    @Autowired
    private MemberFilesSearchRepository mockMemberFilesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberFilesMockMvc;

    private MemberFiles memberFiles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberFiles createEntity(EntityManager em) {
        MemberFiles memberFiles = new MemberFiles()
            .fileName(DEFAULT_FILE_NAME)
            .fileType(DEFAULT_FILE_TYPE)
            .fileContent(DEFAULT_FILE_CONTENT)
            .fileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
        return memberFiles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberFiles createUpdatedEntity(EntityManager em) {
        MemberFiles memberFiles = new MemberFiles()
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        return memberFiles;
    }

    @BeforeEach
    public void initTest() {
        memberFiles = createEntity(em);
    }

    @Test
    @Transactional
    void createMemberFiles() throws Exception {
        int databaseSizeBeforeCreate = memberFilesRepository.findAll().size();
        // Create the MemberFiles
        MemberFilesDTO memberFilesDTO = memberFilesMapper.toDto(memberFiles);
        restMemberFilesMockMvc
            .perform(
                post("/api/member-files")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberFilesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MemberFiles in the database
        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeCreate + 1);
        MemberFiles testMemberFiles = memberFilesList.get(memberFilesList.size() - 1);
        assertThat(testMemberFiles.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testMemberFiles.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testMemberFiles.getFileContent()).isEqualTo(DEFAULT_FILE_CONTENT);
        assertThat(testMemberFiles.getFileContentContentType()).isEqualTo(DEFAULT_FILE_CONTENT_CONTENT_TYPE);

        // Validate the MemberFiles in Elasticsearch
        verify(mockMemberFilesSearchRepository, times(1)).save(testMemberFiles);
    }

    @Test
    @Transactional
    void createMemberFilesWithExistingId() throws Exception {
        // Create the MemberFiles with an existing ID
        memberFiles.setId(1L);
        MemberFilesDTO memberFilesDTO = memberFilesMapper.toDto(memberFiles);

        int databaseSizeBeforeCreate = memberFilesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberFilesMockMvc
            .perform(
                post("/api/member-files")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberFilesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberFiles in the database
        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeCreate);

        // Validate the MemberFiles in Elasticsearch
        verify(mockMemberFilesSearchRepository, times(0)).save(memberFiles);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberFilesRepository.findAll().size();
        // set the field null
        memberFiles.setFileName(null);

        // Create the MemberFiles, which fails.
        MemberFilesDTO memberFilesDTO = memberFilesMapper.toDto(memberFiles);

        restMemberFilesMockMvc
            .perform(
                post("/api/member-files")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberFilesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberFilesRepository.findAll().size();
        // set the field null
        memberFiles.setFileType(null);

        // Create the MemberFiles, which fails.
        MemberFilesDTO memberFilesDTO = memberFilesMapper.toDto(memberFiles);

        restMemberFilesMockMvc
            .perform(
                post("/api/member-files")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberFilesDTO))
            )
            .andExpect(status().isBadRequest());

        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemberFiles() throws Exception {
        // Initialize the database
        memberFilesRepository.saveAndFlush(memberFiles);

        // Get all the memberFilesList
        restMemberFilesMockMvc
            .perform(get("/api/member-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberFiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT))));
    }

    @Test
    @Transactional
    void getMemberFiles() throws Exception {
        // Initialize the database
        memberFilesRepository.saveAndFlush(memberFiles);

        // Get the memberFiles
        restMemberFilesMockMvc
            .perform(get("/api/member-files/{id}", memberFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memberFiles.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
            .andExpect(jsonPath("$.fileContentContentType").value(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileContent").value(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)));
    }

    @Test
    @Transactional
    void getNonExistingMemberFiles() throws Exception {
        // Get the memberFiles
        restMemberFilesMockMvc.perform(get("/api/member-files/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMemberFiles() throws Exception {
        // Initialize the database
        memberFilesRepository.saveAndFlush(memberFiles);

        int databaseSizeBeforeUpdate = memberFilesRepository.findAll().size();

        // Update the memberFiles
        MemberFiles updatedMemberFiles = memberFilesRepository.findById(memberFiles.getId()).get();
        // Disconnect from session so that the updates on updatedMemberFiles are not directly saved in db
        em.detach(updatedMemberFiles);
        updatedMemberFiles
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        MemberFilesDTO memberFilesDTO = memberFilesMapper.toDto(updatedMemberFiles);

        restMemberFilesMockMvc
            .perform(
                put("/api/member-files")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberFilesDTO))
            )
            .andExpect(status().isOk());

        // Validate the MemberFiles in the database
        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeUpdate);
        MemberFiles testMemberFiles = memberFilesList.get(memberFilesList.size() - 1);
        assertThat(testMemberFiles.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testMemberFiles.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testMemberFiles.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testMemberFiles.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);

        // Validate the MemberFiles in Elasticsearch
        verify(mockMemberFilesSearchRepository).save(testMemberFiles);
    }

    @Test
    @Transactional
    void updateNonExistingMemberFiles() throws Exception {
        int databaseSizeBeforeUpdate = memberFilesRepository.findAll().size();

        // Create the MemberFiles
        MemberFilesDTO memberFilesDTO = memberFilesMapper.toDto(memberFiles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberFilesMockMvc
            .perform(
                put("/api/member-files")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberFilesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberFiles in the database
        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MemberFiles in Elasticsearch
        verify(mockMemberFilesSearchRepository, times(0)).save(memberFiles);
    }

    @Test
    @Transactional
    void partialUpdateMemberFilesWithPatch() throws Exception {
        // Initialize the database
        memberFilesRepository.saveAndFlush(memberFiles);

        int databaseSizeBeforeUpdate = memberFilesRepository.findAll().size();

        // Update the memberFiles using partial update
        MemberFiles partialUpdatedMemberFiles = new MemberFiles();
        partialUpdatedMemberFiles.setId(memberFiles.getId());

        partialUpdatedMemberFiles
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE);

        restMemberFilesMockMvc
            .perform(
                patch("/api/member-files")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberFiles))
            )
            .andExpect(status().isOk());

        // Validate the MemberFiles in the database
        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeUpdate);
        MemberFiles testMemberFiles = memberFilesList.get(memberFilesList.size() - 1);
        assertThat(testMemberFiles.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testMemberFiles.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testMemberFiles.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testMemberFiles.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMemberFilesWithPatch() throws Exception {
        // Initialize the database
        memberFilesRepository.saveAndFlush(memberFiles);

        int databaseSizeBeforeUpdate = memberFilesRepository.findAll().size();

        // Update the memberFiles using partial update
        MemberFiles partialUpdatedMemberFiles = new MemberFiles();
        partialUpdatedMemberFiles.setId(memberFiles.getId());

        partialUpdatedMemberFiles
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE);

        restMemberFilesMockMvc
            .perform(
                patch("/api/member-files")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberFiles))
            )
            .andExpect(status().isOk());

        // Validate the MemberFiles in the database
        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeUpdate);
        MemberFiles testMemberFiles = memberFilesList.get(memberFilesList.size() - 1);
        assertThat(testMemberFiles.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testMemberFiles.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testMemberFiles.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testMemberFiles.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void partialUpdateMemberFilesShouldThrown() throws Exception {
        // Update the memberFiles without id should throw
        MemberFiles partialUpdatedMemberFiles = new MemberFiles();

        restMemberFilesMockMvc
            .perform(
                patch("/api/member-files")
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberFiles))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMemberFiles() throws Exception {
        // Initialize the database
        memberFilesRepository.saveAndFlush(memberFiles);

        int databaseSizeBeforeDelete = memberFilesRepository.findAll().size();

        // Delete the memberFiles
        restMemberFilesMockMvc
            .perform(delete("/api/member-files/{id}", memberFiles.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberFiles> memberFilesList = memberFilesRepository.findAll();
        assertThat(memberFilesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MemberFiles in Elasticsearch
        verify(mockMemberFilesSearchRepository, times(1)).deleteById(memberFiles.getId());
    }

    @Test
    @Transactional
    void searchMemberFiles() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        memberFilesRepository.saveAndFlush(memberFiles);
        when(mockMemberFilesSearchRepository.search(queryStringQuery("id:" + memberFiles.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(memberFiles), PageRequest.of(0, 1), 1));

        // Search the memberFiles
        restMemberFilesMockMvc
            .perform(get("/api/_search/member-files?query=id:" + memberFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberFiles.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT))));
    }
}
