package org.emu.membership.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.emu.membership.domain.MembershipStatus;
import org.emu.membership.repository.MembershipStatusRepository;
import org.emu.membership.repository.search.MembershipStatusSearchRepository;
import org.emu.membership.service.MembershipStatusService;
import org.emu.membership.service.dto.MembershipStatusDTO;
import org.emu.membership.service.mapper.MembershipStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MembershipStatus}.
 */
@Service
@Transactional
public class MembershipStatusServiceImpl implements MembershipStatusService {

    private final Logger log = LoggerFactory.getLogger(MembershipStatusServiceImpl.class);

    private final MembershipStatusRepository membershipStatusRepository;

    private final MembershipStatusMapper membershipStatusMapper;

    private final MembershipStatusSearchRepository membershipStatusSearchRepository;

    public MembershipStatusServiceImpl(
        MembershipStatusRepository membershipStatusRepository,
        MembershipStatusMapper membershipStatusMapper,
        MembershipStatusSearchRepository membershipStatusSearchRepository
    ) {
        this.membershipStatusRepository = membershipStatusRepository;
        this.membershipStatusMapper = membershipStatusMapper;
        this.membershipStatusSearchRepository = membershipStatusSearchRepository;
    }

    @Override
    public MembershipStatusDTO save(MembershipStatusDTO membershipStatusDTO) {
        log.debug("Request to save MembershipStatus : {}", membershipStatusDTO);
        MembershipStatus membershipStatus = membershipStatusMapper.toEntity(membershipStatusDTO);
        membershipStatus = membershipStatusRepository.save(membershipStatus);
        MembershipStatusDTO result = membershipStatusMapper.toDto(membershipStatus);
        membershipStatusSearchRepository.save(membershipStatus);
        return result;
    }

    @Override
    public Optional<MembershipStatusDTO> partialUpdate(MembershipStatusDTO membershipStatusDTO) {
        log.debug("Request to partially update MembershipStatus : {}", membershipStatusDTO);

        return membershipStatusRepository
            .findById(membershipStatusDTO.getId())
            .map(
                existingMembershipStatus -> {
                    if (membershipStatusDTO.getName() != null) {
                        existingMembershipStatus.setName(membershipStatusDTO.getName());
                    }

                    if (membershipStatusDTO.getDescription() != null) {
                        existingMembershipStatus.setDescription(membershipStatusDTO.getDescription());
                    }

                    return existingMembershipStatus;
                }
            )
            .map(membershipStatusRepository::save)
            .map(
                savedMembershipStatus -> {
                    membershipStatusSearchRepository.save(savedMembershipStatus);

                    return savedMembershipStatus;
                }
            )
            .map(membershipStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MembershipStatuses");
        return membershipStatusRepository.findAll(pageable).map(membershipStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipStatusDTO> findOne(Long id) {
        log.debug("Request to get MembershipStatus : {}", id);
        return membershipStatusRepository.findById(id).map(membershipStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MembershipStatus : {}", id);
        membershipStatusRepository.deleteById(id);
        membershipStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MembershipStatuses for query {}", query);
        return membershipStatusSearchRepository.search(queryStringQuery(query), pageable).map(membershipStatusMapper::toDto);
    }
}
