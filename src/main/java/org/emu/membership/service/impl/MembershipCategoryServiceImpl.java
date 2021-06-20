package org.emu.membership.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.emu.membership.domain.MembershipCategory;
import org.emu.membership.repository.MembershipCategoryRepository;
import org.emu.membership.repository.search.MembershipCategorySearchRepository;
import org.emu.membership.service.MembershipCategoryService;
import org.emu.membership.service.dto.MembershipCategoryDTO;
import org.emu.membership.service.mapper.MembershipCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MembershipCategory}.
 */
@Service
@Transactional
public class MembershipCategoryServiceImpl implements MembershipCategoryService {

    private final Logger log = LoggerFactory.getLogger(MembershipCategoryServiceImpl.class);

    private final MembershipCategoryRepository membershipCategoryRepository;

    private final MembershipCategoryMapper membershipCategoryMapper;

    private final MembershipCategorySearchRepository membershipCategorySearchRepository;

    public MembershipCategoryServiceImpl(
        MembershipCategoryRepository membershipCategoryRepository,
        MembershipCategoryMapper membershipCategoryMapper,
        MembershipCategorySearchRepository membershipCategorySearchRepository
    ) {
        this.membershipCategoryRepository = membershipCategoryRepository;
        this.membershipCategoryMapper = membershipCategoryMapper;
        this.membershipCategorySearchRepository = membershipCategorySearchRepository;
    }

    @Override
    public MembershipCategoryDTO save(MembershipCategoryDTO membershipCategoryDTO) {
        log.debug("Request to save MembershipCategory : {}", membershipCategoryDTO);
        MembershipCategory membershipCategory = membershipCategoryMapper.toEntity(membershipCategoryDTO);
        membershipCategory = membershipCategoryRepository.save(membershipCategory);
        MembershipCategoryDTO result = membershipCategoryMapper.toDto(membershipCategory);
        membershipCategorySearchRepository.save(membershipCategory);
        return result;
    }

    @Override
    public Optional<MembershipCategoryDTO> partialUpdate(MembershipCategoryDTO membershipCategoryDTO) {
        log.debug("Request to partially update MembershipCategory : {}", membershipCategoryDTO);

        return membershipCategoryRepository
            .findById(membershipCategoryDTO.getId())
            .map(
                existingMembershipCategory -> {
                    if (membershipCategoryDTO.getName() != null) {
                        existingMembershipCategory.setName(membershipCategoryDTO.getName());
                    }

                    if (membershipCategoryDTO.getDescription() != null) {
                        existingMembershipCategory.setDescription(membershipCategoryDTO.getDescription());
                    }

                    return existingMembershipCategory;
                }
            )
            .map(membershipCategoryRepository::save)
            .map(
                savedMembershipCategory -> {
                    membershipCategorySearchRepository.save(savedMembershipCategory);

                    return savedMembershipCategory;
                }
            )
            .map(membershipCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MembershipCategories");
        return membershipCategoryRepository.findAll(pageable).map(membershipCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipCategoryDTO> findOne(Long id) {
        log.debug("Request to get MembershipCategory : {}", id);
        return membershipCategoryRepository.findById(id).map(membershipCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MembershipCategory : {}", id);
        membershipCategoryRepository.deleteById(id);
        membershipCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MembershipCategories for query {}", query);
        return membershipCategorySearchRepository.search(queryStringQuery(query), pageable).map(membershipCategoryMapper::toDto);
    }
}
