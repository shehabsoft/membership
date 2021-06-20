package org.emu.membership.service;

import java.util.Optional;
import org.emu.membership.service.dto.MembershipCategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.emu.membership.domain.MembershipCategory}.
 */
public interface MembershipCategoryService {
    /**
     * Save a membershipCategory.
     *
     * @param membershipCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipCategoryDTO save(MembershipCategoryDTO membershipCategoryDTO);

    /**
     * Partially updates a membershipCategory.
     *
     * @param membershipCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MembershipCategoryDTO> partialUpdate(MembershipCategoryDTO membershipCategoryDTO);

    /**
     * Get all the membershipCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" membershipCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" membershipCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the membershipCategory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MembershipCategoryDTO> search(String query, Pageable pageable);
}
