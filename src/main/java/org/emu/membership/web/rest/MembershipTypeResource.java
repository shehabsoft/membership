package org.emu.membership.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.emu.membership.service.MembershipTypeService;
import org.emu.membership.service.dto.MembershipTypeDTO;
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
 * REST controller for managing {@link org.emu.membership.domain.MembershipType}.
 */
@RestController
@RequestMapping("/api")
public class MembershipTypeResource {

    private final Logger log = LoggerFactory.getLogger(MembershipTypeResource.class);

    private static final String ENTITY_NAME = "membershipMembershipType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipTypeService membershipTypeService;

    public MembershipTypeResource(MembershipTypeService membershipTypeService) {
        this.membershipTypeService = membershipTypeService;
    }

    /**
     * {@code POST  /membership-types} : Create a new membershipType.
     *
     * @param membershipTypeDTO the membershipTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipTypeDTO, or with status {@code 400 (Bad Request)} if the membershipType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-types")
    public ResponseEntity<MembershipTypeDTO> createMembershipType(@Valid @RequestBody MembershipTypeDTO membershipTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save MembershipType : {}", membershipTypeDTO);
        if (membershipTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipTypeDTO result = membershipTypeService.save(membershipTypeDTO);
        return ResponseEntity
            .created(new URI("/api/membership-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-types} : Updates an existing membershipType.
     *
     * @param membershipTypeDTO the membershipTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipTypeDTO,
     * or with status {@code 400 (Bad Request)} if the membershipTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-types")
    public ResponseEntity<MembershipTypeDTO> updateMembershipType(@Valid @RequestBody MembershipTypeDTO membershipTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to update MembershipType : {}", membershipTypeDTO);
        if (membershipTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MembershipTypeDTO result = membershipTypeService.save(membershipTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membership-types} : Updates given fields of an existing membershipType.
     *
     * @param membershipTypeDTO the membershipTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipTypeDTO,
     * or with status {@code 400 (Bad Request)} if the membershipTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membershipTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/membership-types", consumes = "application/merge-patch+json")
    public ResponseEntity<MembershipTypeDTO> partialUpdateMembershipType(@NotNull @RequestBody MembershipTypeDTO membershipTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to update MembershipType partially : {}", membershipTypeDTO);
        if (membershipTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<MembershipTypeDTO> result = membershipTypeService.partialUpdate(membershipTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-types} : get all the membershipTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipTypes in body.
     */
    @GetMapping("/membership-types")
    public ResponseEntity<List<MembershipTypeDTO>> getAllMembershipTypes(Pageable pageable) {
        log.debug("REST request to get a page of MembershipTypes");
        Page<MembershipTypeDTO> page = membershipTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /membership-types/:id} : get the "id" membershipType.
     *
     * @param id the id of the membershipTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-types/{id}")
    public ResponseEntity<MembershipTypeDTO> getMembershipType(@PathVariable Long id) {
        log.debug("REST request to get MembershipType : {}", id);
        Optional<MembershipTypeDTO> membershipTypeDTO = membershipTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipTypeDTO);
    }

    /**
     * {@code DELETE  /membership-types/:id} : delete the "id" membershipType.
     *
     * @param id the id of the membershipTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-types/{id}")
    public ResponseEntity<Void> deleteMembershipType(@PathVariable Long id) {
        log.debug("REST request to delete MembershipType : {}", id);
        membershipTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/membership-types?query=:query} : search for the membershipType corresponding
     * to the query.
     *
     * @param query the query of the membershipType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/membership-types")
    public ResponseEntity<List<MembershipTypeDTO>> searchMembershipTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MembershipTypes for query {}", query);
        Page<MembershipTypeDTO> page = membershipTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
