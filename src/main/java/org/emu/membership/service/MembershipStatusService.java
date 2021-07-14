package org.emu.membership.service;

import java.util.Optional;
import org.emu.membership.service.dto.MembershipStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.emu.membership.domain.MembershipStatus}.
 */
public interface MembershipStatusService {
    /**
     * Save a membershipStatus.
     *
     * @param membershipStatusDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipStatusDTO save(MembershipStatusDTO membershipStatusDTO);

    /**
     * Partially updates a membershipStatus.
     *
     * @param membershipStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MembershipStatusDTO> partialUpdate(MembershipStatusDTO membershipStatusDTO);

    /**
     * Get all the membershipStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipStatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" membershipStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipStatusDTO> findOne(Long id);

    /**
     * Delete the "id" membershipStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the membershipStatus corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipStatusDTO> search(String query, Pageable pageable);
}
