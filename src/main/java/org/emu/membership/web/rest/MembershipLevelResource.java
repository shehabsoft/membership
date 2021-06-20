package org.emu.membership.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.emu.membership.service.MembershipLevelService;
import org.emu.membership.service.dto.MembershipLevelDTO;
import org.emu.membership.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.emu.membership.domain.MembershipLevel}.
 */
@RestController
@RequestMapping("/api")
public class MembershipLevelResource {

    private final Logger log = LoggerFactory.getLogger(MembershipLevelResource.class);

    private static final String ENTITY_NAME = "membershipMembershipLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipLevelService membershipLevelService;

    public MembershipLevelResource(MembershipLevelService membershipLevelService) {
        this.membershipLevelService = membershipLevelService;
    }

    /**
     * {@code POST  /membership-levels} : Create a new membershipLevel.
     *
     * @param membershipLevelDTO the membershipLevelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipLevelDTO, or with status {@code 400 (Bad Request)} if the membershipLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-levels")
    public ResponseEntity<MembershipLevelDTO> createMembershipLevel(@Valid @RequestBody MembershipLevelDTO membershipLevelDTO)
        throws URISyntaxException {
        log.debug("REST request to save MembershipLevel : {}", membershipLevelDTO);
        if (membershipLevelDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipLevelDTO result = membershipLevelService.save(membershipLevelDTO);
        return ResponseEntity
            .created(new URI("/api/membership-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-levels} : Updates an existing membershipLevel.
     *
     * @param membershipLevelDTO the membershipLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipLevelDTO,
     * or with status {@code 400 (Bad Request)} if the membershipLevelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-levels")
    public ResponseEntity<MembershipLevelDTO> updateMembershipLevel(@Valid @RequestBody MembershipLevelDTO membershipLevelDTO)
        throws URISyntaxException {
        log.debug("REST request to update MembershipLevel : {}", membershipLevelDTO);
        if (membershipLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MembershipLevelDTO result = membershipLevelService.save(membershipLevelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipLevelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membership-levels} : Updates given fields of an existing membershipLevel.
     *
     * @param membershipLevelDTO the membershipLevelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipLevelDTO,
     * or with status {@code 400 (Bad Request)} if the membershipLevelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membershipLevelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipLevelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/membership-levels", consumes = "application/merge-patch+json")
    public ResponseEntity<MembershipLevelDTO> partialUpdateMembershipLevel(@NotNull @RequestBody MembershipLevelDTO membershipLevelDTO)
        throws URISyntaxException {
        log.debug("REST request to update MembershipLevel partially : {}", membershipLevelDTO);
        if (membershipLevelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<MembershipLevelDTO> result = membershipLevelService.partialUpdate(membershipLevelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipLevelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-levels} : get all the membershipLevels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipLevels in body.
     */
    @GetMapping("/membership-levels")
    public ResponseEntity<List<MembershipLevelDTO>> getAllMembershipLevels(Pageable pageable) {
        log.debug("REST request to get a page of MembershipLevels");
        Page<MembershipLevelDTO> page = membershipLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /membership-levels/:id} : get the "id" membershipLevel.
     *
     * @param id the id of the membershipLevelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipLevelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-levels/{id}")
    public ResponseEntity<MembershipLevelDTO> getMembershipLevel(@PathVariable Long id) {
        log.debug("REST request to get MembershipLevel : {}", id);
        Optional<MembershipLevelDTO> membershipLevelDTO = membershipLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipLevelDTO);
    }

    /**
     * {@code DELETE  /membership-levels/:id} : delete the "id" membershipLevel.
     *
     * @param id the id of the membershipLevelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-levels/{id}")
    public ResponseEntity<Void> deleteMembershipLevel(@PathVariable Long id) {
        log.debug("REST request to delete MembershipLevel : {}", id);
        membershipLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/membership-levels?query=:query} : search for the membershipLevel corresponding
     * to the query.
     *
     * @param query the query of the membershipLevel search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/membership-levels")
    public ResponseEntity<List<MembershipLevelDTO>> searchMembershipLevels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MembershipLevels for query {}", query);
        Page<MembershipLevelDTO> page = membershipLevelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
