package org.emu.membership.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.emu.membership.service.MemberFilesService;
import org.emu.membership.service.dto.MemberDTO;
import org.emu.membership.service.dto.MemberFilesDTO;
import org.emu.membership.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.emu.membership.domain.MemberFiles}.
 */
@RestController
@RequestMapping("/api")
public class MemberFilesResource {

    private final Logger log = LoggerFactory.getLogger(MemberFilesResource.class);

    private static final String ENTITY_NAME = "membershipMemberFiles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberFilesService memberFilesService;

    public MemberFilesResource(MemberFilesService memberFilesService) {
        this.memberFilesService = memberFilesService;
    }

    /**
     * {@code POST  /member-files} : Create a new memberFiles.
     *
     * @param memberFilesDTO the memberFilesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberFilesDTO, or with status {@code 400 (Bad Request)} if the memberFiles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-files")
    public ResponseEntity<MemberFilesDTO> createMemberFiles(@Valid @RequestBody MemberFilesDTO memberFilesDTO) throws URISyntaxException {
        log.debug("REST request to save MemberFiles : {}", memberFilesDTO);
        if (memberFilesDTO.getId() != null) {
            throw new BadRequestAlertException("A new memberFiles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberFilesDTO result = memberFilesService.save(memberFilesDTO);
        return ResponseEntity
            .created(new URI("/api/member-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-files} : Updates an existing memberFiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberFilesDTO,
     * or with status {@code 400 (Bad Request)} if the memberFilesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberFilesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(path = "/member-files", consumes = "multipart/form-data")
    public ResponseEntity<MemberFilesDTO> createMemberFiles(@RequestPart("file") List<MultipartFile> mFileList)
        throws URISyntaxException, IOException {
        // log.debug("REST request to save MemberFiles : {}", memberFilesDTO);
        //        if (memberFilesDTO.getId() != null) {
        //            throw new BadRequestAlertException("A new memberFiles cannot already have an ID", ENTITY_NAME, "idexists");
        //        }
        MemberFilesDTO result = null;
        for (int i = 0; i < mFileList.size(); i++) {
            MultipartFile mFile = mFileList.get(i);
            MemberFilesDTO memberFilesDT = new MemberFilesDTO();

            memberFilesDT.setFileContent(mFile.getBytes());
            memberFilesDT.setFileName(mFile.getName());
            memberFilesDT.setFileType(mFile.getContentType());
            memberFilesDT.setFileContentContentType(mFile.getContentType());
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setId(1L);
            memberFilesDT.setMember(memberDTO);
            result = memberFilesService.save(memberFilesDT);
        }

        return ResponseEntity
            .created(new URI("/api/member-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /member-files} : Updates given fields of an existing memberFiles.
     *
     * @param memberFilesDTO the memberFilesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberFilesDTO,
     * or with status {@code 400 (Bad Request)} if the memberFilesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memberFilesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memberFilesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/member-files", consumes = "application/merge-patch+json")
    public ResponseEntity<MemberFilesDTO> partialUpdateMemberFiles(@NotNull @RequestBody MemberFilesDTO memberFilesDTO)
        throws URISyntaxException {
        log.debug("REST request to update MemberFiles partially : {}", memberFilesDTO);
        if (memberFilesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<MemberFilesDTO> result = memberFilesService.partialUpdate(memberFilesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberFilesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /member-files} : get all the memberFiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberFiles in body.
     */
    @GetMapping("/member-files")
    public ResponseEntity<List<MemberFilesDTO>> getAllMemberFiles(Pageable pageable) {
        log.debug("REST request to get a page of MemberFiles");
        Page<MemberFilesDTO> page = memberFilesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /member-files/:id} : get the "id" memberFiles.
     *
     * @param id the id of the memberFilesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberFilesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-files/{id}")
    public ResponseEntity<MemberFilesDTO> getMemberFiles(@PathVariable Long id) {
        log.debug("REST request to get MemberFiles : {}", id);
        Optional<MemberFilesDTO> memberFilesDTO = memberFilesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberFilesDTO);
    }

    /**
     * {@code DELETE  /member-files/:id} : delete the "id" memberFiles.
     *
     * @param id the id of the memberFilesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-files/{id}")
    public ResponseEntity<Void> deleteMemberFiles(@PathVariable Long id) {
        log.debug("REST request to delete MemberFiles : {}", id);
        memberFilesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/member-files?query=:query} : search for the memberFiles corresponding
     * to the query.
     *
     * @param query the query of the memberFiles search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/member-files")
    public ResponseEntity<List<MemberFilesDTO>> searchMemberFiles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MemberFiles for query {}", query);
        Page<MemberFilesDTO> page = memberFilesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
