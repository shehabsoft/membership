package org.emu.membership.service;

import java.util.Optional;
import org.emu.membership.service.dto.MembershipTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.emu.membership.domain.MembershipType}.
 */
public interface MembershipTypeService {
    /**
     * Save a membershipType.
     *
     * @param membershipTypeDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipTypeDTO save(MembershipTypeDTO membershipTypeDTO);

    /**
     * Partially updates a membershipType.
     *
     * @param membershipTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MembershipTypeDTO> partialUpdate(MembershipTypeDTO membershipTypeDTO);

    /**
     * Get all the membershipTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" membershipType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipTypeDTO> findOne(Long id);

    /**
     * Delete the "id" membershipType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the membershipType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipTypeDTO> search(String query, Pageable pageable);
}
