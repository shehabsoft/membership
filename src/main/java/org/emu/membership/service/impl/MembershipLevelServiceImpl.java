package org.emu.membership.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.emu.membership.domain.MembershipLevel;
import org.emu.membership.repository.MembershipLevelRepository;
import org.emu.membership.repository.search.MembershipLevelSearchRepository;
import org.emu.membership.service.MembershipLevelService;
import org.emu.membership.service.dto.MembershipLevelDTO;
import org.emu.membership.service.mapper.MembershipLevelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MembershipLevel}.
 */
@Service
@Transactional
public class MembershipLevelServiceImpl implements MembershipLevelService {

    private final Logger log = LoggerFactory.getLogger(MembershipLevelServiceImpl.class);

    private final MembershipLevelRepository membershipLevelRepository;

    private final MembershipLevelMapper membershipLevelMapper;

    private final MembershipLevelSearchRepository membershipLevelSearchRepository;

    public MembershipLevelServiceImpl(
        MembershipLevelRepository membershipLevelRepository,
        MembershipLevelMapper membershipLevelMapper,
        MembershipLevelSearchRepository membershipLevelSearchRepository
    ) {
        this.membershipLevelRepository = membershipLevelRepository;
        this.membershipLevelMapper = membershipLevelMapper;
        this.membershipLevelSearchRepository = membershipLevelSearchRepository;
    }

    @Override
    public MembershipLevelDTO save(MembershipLevelDTO membershipLevelDTO) {
        log.debug("Request to save MembershipLevel : {}", membershipLevelDTO);
        MembershipLevel membershipLevel = membershipLevelMapper.toEntity(membershipLevelDTO);
        membershipLevel = membershipLevelRepository.save(membershipLevel);
        MembershipLevelDTO result = membershipLevelMapper.toDto(membershipLevel);
        membershipLevelSearchRepository.save(membershipLevel);
        return result;
    }

    @Override
    public Optional<MembershipLevelDTO> partialUpdate(MembershipLevelDTO membershipLevelDTO) {
        log.debug("Request to partially update MembershipLevel : {}", membershipLevelDTO);

        return membershipLevelRepository
            .findById(membershipLevelDTO.getId())
            .map(
                existingMembershipLevel -> {
                    if (membershipLevelDTO.getName() != null) {
                        existingMembershipLevel.setName(membershipLevelDTO.getName());
                    }

                    if (membershipLevelDTO.getDescription() != null) {
                        existingMembershipLevel.setDescription(membershipLevelDTO.getDescription());
                    }

                    return existingMembershipLevel;
                }
            )
            .map(membershipLevelRepository::save)
            .map(
                savedMembershipLevel -> {
                    membershipLevelSearchRepository.save(savedMembershipLevel);

                    return savedMembershipLevel;
                }
            )
            .map(membershipLevelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipLevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MembershipLevels");
        return membershipLevelRepository.findAll(pageable).map(membershipLevelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipLevelDTO> findOne(Long id) {
        log.debug("Request to get MembershipLevel : {}", id);
        return membershipLevelRepository.findById(id).map(membershipLevelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MembershipLevel : {}", id);
        membershipLevelRepository.deleteById(id);
        membershipLevelSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipLevelDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MembershipLevels for query {}", query);
        return membershipLevelSearchRepository.search(queryStringQuery(query), pageable).map(membershipLevelMapper::toDto);
    }
}
