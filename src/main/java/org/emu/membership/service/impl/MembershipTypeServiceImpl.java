package org.emu.membership.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.emu.membership.domain.MembershipType;
import org.emu.membership.repository.MembershipTypeRepository;
import org.emu.membership.repository.search.MembershipTypeSearchRepository;
import org.emu.membership.service.MembershipTypeService;
import org.emu.membership.service.dto.MembershipTypeDTO;
import org.emu.membership.service.mapper.MembershipTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MembershipType}.
 */
@Service
@Transactional
public class MembershipTypeServiceImpl implements MembershipTypeService {

    private final Logger log = LoggerFactory.getLogger(MembershipTypeServiceImpl.class);

    private final MembershipTypeRepository membershipTypeRepository;

    private final MembershipTypeMapper membershipTypeMapper;

    private final MembershipTypeSearchRepository membershipTypeSearchRepository;

    public MembershipTypeServiceImpl(
        MembershipTypeRepository membershipTypeRepository,
        MembershipTypeMapper membershipTypeMapper,
        MembershipTypeSearchRepository membershipTypeSearchRepository
    ) {
        this.membershipTypeRepository = membershipTypeRepository;
        this.membershipTypeMapper = membershipTypeMapper;
        this.membershipTypeSearchRepository = membershipTypeSearchRepository;
    }

    @Override
    public MembershipTypeDTO save(MembershipTypeDTO membershipTypeDTO) {
        log.debug("Request to save MembershipType : {}", membershipTypeDTO);
        MembershipType membershipType = membershipTypeMapper.toEntity(membershipTypeDTO);
        membershipType = membershipTypeRepository.save(membershipType);
        MembershipTypeDTO result = membershipTypeMapper.toDto(membershipType);
        membershipTypeSearchRepository.save(membershipType);
        return result;
    }

    @Override
    public Optional<MembershipTypeDTO> partialUpdate(MembershipTypeDTO membershipTypeDTO) {
        log.debug("Request to partially update MembershipType : {}", membershipTypeDTO);

        return membershipTypeRepository
            .findById(membershipTypeDTO.getId())
            .map(
                existingMembershipType -> {
                    if (membershipTypeDTO.getName() != null) {
                        existingMembershipType.setName(membershipTypeDTO.getName());
                    }

                    if (membershipTypeDTO.getDescription() != null) {
                        existingMembershipType.setDescription(membershipTypeDTO.getDescription());
                    }

                    return existingMembershipType;
                }
            )
            .map(membershipTypeRepository::save)
            .map(
                savedMembershipType -> {
                    membershipTypeSearchRepository.save(savedMembershipType);

                    return savedMembershipType;
                }
            )
            .map(membershipTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MembershipTypes");
        return membershipTypeRepository.findAll(pageable).map(membershipTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipTypeDTO> findOne(Long id) {
        log.debug("Request to get MembershipType : {}", id);
        return membershipTypeRepository.findById(id).map(membershipTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MembershipType : {}", id);
        membershipTypeRepository.deleteById(id);
        membershipTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MembershipTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MembershipTypes for query {}", query);
        return membershipTypeSearchRepository.search(queryStringQuery(query), pageable).map(membershipTypeMapper::toDto);
    }
}
