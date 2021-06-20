package org.emu.membership.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.emu.membership.service.MembershipCategoryService;
import org.emu.membership.service.dto.MembershipCategoryDTO;
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
 * REST controller for managing {@link org.emu.membership.domain.MembershipCategory}.
 */
@RestController
@RequestMapping("/api")
public class MembershipCategoryResource {

    private final Logger log = LoggerFactory.getLogger(MembershipCategoryResource.class);

    private static final String ENTITY_NAME = "membershipMembershipCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipCategoryService membershipCategoryService;

    public MembershipCategoryResource(MembershipCategoryService membershipCategoryService) {
        this.membershipCategoryService = membershipCategoryService;
    }

    /**
     * {@code POST  /membership-categories} : Create a new membershipCategory.
     *
     * @param membershipCategoryDTO the membershipCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipCategoryDTO, or with status {@code 400 (Bad Request)} if the membershipCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-categories")
    public ResponseEntity<MembershipCategoryDTO> createMembershipCategory(@Valid @RequestBody MembershipCategoryDTO membershipCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save MembershipCategory : {}", membershipCategoryDTO);
        if (membershipCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipCategoryDTO result = membershipCategoryService.save(membershipCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/membership-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-categories} : Updates an existing membershipCategory.
     *
     * @param membershipCategoryDTO the membershipCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the membershipCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-categories")
    public ResponseEntity<MembershipCategoryDTO> updateMembershipCategory(@Valid @RequestBody MembershipCategoryDTO membershipCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to update MembershipCategory : {}", membershipCategoryDTO);
        if (membershipCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MembershipCategoryDTO result = membershipCategoryService.save(membershipCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membership-categories} : Updates given fields of an existing membershipCategory.
     *
     * @param membershipCategoryDTO the membershipCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the membershipCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membershipCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/membership-categories", consumes = "application/merge-patch+json")
    public ResponseEntity<MembershipCategoryDTO> partialUpdateMembershipCategory(
        @NotNull @RequestBody MembershipCategoryDTO membershipCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MembershipCategory partially : {}", membershipCategoryDTO);
        if (membershipCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<MembershipCategoryDTO> result = membershipCategoryService.partialUpdate(membershipCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-categories} : get all the membershipCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipCategories in body.
     */
    @GetMapping("/membership-categories")
    public ResponseEntity<List<MembershipCategoryDTO>> getAllMembershipCategories(Pageable pageable) {
        log.debug("REST request to get a page of MembershipCategories");
        Page<MembershipCategoryDTO> page = membershipCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /membership-categories/:id} : get the "id" membershipCategory.
     *
     * @param id the id of the membershipCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-categories/{id}")
    public ResponseEntity<MembershipCategoryDTO> getMembershipCategory(@PathVariable Long id) {
        log.debug("REST request to get MembershipCategory : {}", id);
        Optional<MembershipCategoryDTO> membershipCategoryDTO = membershipCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipCategoryDTO);
    }

    /**
     * {@code DELETE  /membership-categories/:id} : delete the "id" membershipCategory.
     *
     * @param id the id of the membershipCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-categories/{id}")
    public ResponseEntity<Void> deleteMembershipCategory(@PathVariable Long id) {
        log.debug("REST request to delete MembershipCategory : {}", id);
        membershipCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/membership-categories?query=:query} : search for the membershipCategory corresponding
     * to the query.
     *
     * @param query the query of the membershipCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/membership-categories")
    public ResponseEntity<List<MembershipCategoryDTO>> searchMembershipCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MembershipCategories for query {}", query);
        Page<MembershipCategoryDTO> page = membershipCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
