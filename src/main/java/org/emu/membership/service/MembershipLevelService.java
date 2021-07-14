package org.emu.membership.service;

import java.util.Optional;
import org.emu.membership.service.dto.MembershipLevelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.emu.membership.domain.MembershipLevel}.
 */
public interface MembershipLevelService {
    /**
     * Save a membershipLevel.
     *
     * @param membershipLevelDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipLevelDTO save(MembershipLevelDTO membershipLevelDTO);

    /**
     * Partially updates a membershipLevel.
     *
     * @param membershipLevelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MembershipLevelDTO> partialUpdate(MembershipLevelDTO membershipLevelDTO);

    /**
     * Get all the membershipLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipLevelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" membershipLevel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipLevelDTO> findOne(Long id);

    /**
     * Delete the "id" membershipLevel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the membershipLevel corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipLevelDTO> search(String query, Pageable pageable);
}
