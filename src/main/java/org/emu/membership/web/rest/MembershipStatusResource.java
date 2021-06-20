package org.emu.membership.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.emu.membership.service.MembershipStatusService;
import org.emu.membership.service.dto.MembershipStatusDTO;
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
 * REST controller for managing {@link org.emu.membership.domain.MembershipStatus}.
 */
@RestController
@RequestMapping("/api")
public class MembershipStatusResource {

    private final Logger log = LoggerFactory.getLogger(MembershipStatusResource.class);

    private static final String ENTITY_NAME = "membershipMembershipStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipStatusService membershipStatusService;

    public MembershipStatusResource(MembershipStatusService membershipStatusService) {
        this.membershipStatusService = membershipStatusService;
    }

    /**
     * {@code POST  /membership-statuses} : Create a new membershipStatus.
     *
     * @param membershipStatusDTO the membershipStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipStatusDTO, or with status {@code 400 (Bad Request)} if the membershipStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-statuses")
    public ResponseEntity<MembershipStatusDTO> createMembershipStatus(@Valid @RequestBody MembershipStatusDTO membershipStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save MembershipStatus : {}", membershipStatusDTO);
        if (membershipStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipStatusDTO result = membershipStatusService.save(membershipStatusDTO);
        return ResponseEntity
            .created(new URI("/api/membership-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-statuses} : Updates an existing membershipStatus.
     *
     * @param membershipStatusDTO the membershipStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipStatusDTO,
     * or with status {@code 400 (Bad Request)} if the membershipStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-statuses")
    public ResponseEntity<MembershipStatusDTO> updateMembershipStatus(@Valid @RequestBody MembershipStatusDTO membershipStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to update MembershipStatus : {}", membershipStatusDTO);
        if (membershipStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MembershipStatusDTO result = membershipStatusService.save(membershipStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membership-statuses} : Updates given fields of an existing membershipStatus.
     *
     * @param membershipStatusDTO the membershipStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipStatusDTO,
     * or with status {@code 400 (Bad Request)} if the membershipStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membershipStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/membership-statuses", consumes = "application/merge-patch+json")
    public ResponseEntity<MembershipStatusDTO> partialUpdateMembershipStatus(@NotNull @RequestBody MembershipStatusDTO membershipStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to update MembershipStatus partially : {}", membershipStatusDTO);
        if (membershipStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<MembershipStatusDTO> result = membershipStatusService.partialUpdate(membershipStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-statuses} : get all the membershipStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipStatuses in body.
     */
    @GetMapping("/membership-statuses")
    public ResponseEntity<List<MembershipStatusDTO>> getAllMembershipStatuses(Pageable pageable) {
        log.debug("REST request to get a page of MembershipStatuses");
        Page<MembershipStatusDTO> page = membershipStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /membership-statuses/:id} : get the "id" membershipStatus.
     *
     * @param id the id of the membershipStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-statuses/{id}")
    public ResponseEntity<MembershipStatusDTO> getMembershipStatus(@PathVariable Long id) {
        log.debug("REST request to get MembershipStatus : {}", id);
        Optional<MembershipStatusDTO> membershipStatusDTO = membershipStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipStatusDTO);
    }

    /**
     * {@code DELETE  /membership-statuses/:id} : delete the "id" membershipStatus.
     *
     * @param id the id of the membershipStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-statuses/{id}")
    public ResponseEntity<Void> deleteMembershipStatus(@PathVariable Long id) {
        log.debug("REST request to delete MembershipStatus : {}", id);
        membershipStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/membership-statuses?query=:query} : search for the membershipStatus corresponding
     * to the query.
     *
     * @param query the query of the membershipStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/membership-statuses")
    public ResponseEntity<List<MembershipStatusDTO>> searchMembershipStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MembershipStatuses for query {}", query);
        Page<MembershipStatusDTO> page = membershipStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
