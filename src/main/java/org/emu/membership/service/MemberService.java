package org.emu.membership.service;

import java.util.List;
import java.util.Optional;
import org.emu.membership.domain.Member;
import org.emu.membership.service.dto.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

/**
 * Service Interface for managing {@link org.emu.membership.domain.Member}.
 */
public interface MemberService {
    /**
     * Save a member.
     *
     * @param memberDTO the entity to save.
     * @return the persisted entity.
     */
    MemberDTO save(MemberDTO memberDTO);

    /**
     * Partially updates a member.
     *
     * @param memberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MemberDTO> partialUpdate(MemberDTO memberDTO);

    /**
     * Get all the members.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemberDTO> findAll(Pageable pageable);

    /**
     * Get the "id" member.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemberDTO> findOne(Long id);

    /**
     * Delete the "id" member.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the member corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MemberDTO> search(String query, Pageable pageable);

    List<Member> searchByFirstName(String key);
    List<SearchHits<?>> searchInMultipleIdxs(String query) throws ClassNotFoundException;
}
