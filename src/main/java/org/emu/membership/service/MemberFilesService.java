package org.emu.membership.service;

import java.util.Optional;
import org.emu.membership.service.dto.MemberFilesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.emu.membership.domain.MemberFiles}.
 */
public interface MemberFilesService {
    /**
     * Save a memberFiles.
     *
     * @param memberFilesDTO the entity to save.
     * @return the persisted entity.
     */
    MemberFilesDTO save(MemberFilesDTO memberFilesDTO);

    /**
     * Partially updates a memberFiles.
     *
     * @param memberFilesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MemberFilesDTO> partialUpdate(MemberFilesDTO memberFilesDTO);

    /**
     * Get all the memberFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemberFilesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" memberFiles.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemberFilesDTO> findOne(Long id);

    /**
     * Delete the "id" memberFiles.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the memberFiles corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemberFilesDTO> search(String query, Pageable pageable);
}
