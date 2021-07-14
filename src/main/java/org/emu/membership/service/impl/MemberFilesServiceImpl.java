package org.emu.membership.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.Optional;
import org.emu.membership.domain.MemberFiles;
import org.emu.membership.repository.MemberFilesRepository;
import org.emu.membership.repository.search.MemberFilesSearchRepository;
import org.emu.membership.service.MemberFilesService;
import org.emu.membership.service.dto.MemberFilesDTO;
import org.emu.membership.service.mapper.MemberFilesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MemberFiles}.
 */
@Service
@Transactional
public class MemberFilesServiceImpl implements MemberFilesService {

    private final Logger log = LoggerFactory.getLogger(MemberFilesServiceImpl.class);

    private final MemberFilesRepository memberFilesRepository;

    private final MemberFilesMapper memberFilesMapper;

    private final MemberFilesSearchRepository memberFilesSearchRepository;

    public MemberFilesServiceImpl(
        MemberFilesRepository memberFilesRepository,
        MemberFilesMapper memberFilesMapper,
        MemberFilesSearchRepository memberFilesSearchRepository
    ) {
        this.memberFilesRepository = memberFilesRepository;
        this.memberFilesMapper = memberFilesMapper;
        this.memberFilesSearchRepository = memberFilesSearchRepository;
    }

    @Override
    public MemberFilesDTO save(MemberFilesDTO memberFilesDTO) {
        log.debug("Request to save MemberFiles : {}", memberFilesDTO);
        MemberFiles memberFiles = memberFilesMapper.toEntity(memberFilesDTO);
        memberFiles = memberFilesRepository.save(memberFiles);
        MemberFilesDTO result = memberFilesMapper.toDto(memberFiles);
        memberFilesSearchRepository.save(memberFiles);
        return result;
    }

    @Override
    public Optional<MemberFilesDTO> partialUpdate(MemberFilesDTO memberFilesDTO) {
        log.debug("Request to partially update MemberFiles : {}", memberFilesDTO);

        return memberFilesRepository
            .findById(memberFilesDTO.getId())
            .map(
                existingMemberFiles -> {
                    if (memberFilesDTO.getFileName() != null) {
                        existingMemberFiles.setFileName(memberFilesDTO.getFileName());
                    }

                    if (memberFilesDTO.getFileType() != null) {
                        existingMemberFiles.setFileType(memberFilesDTO.getFileType());
                    }

                    if (memberFilesDTO.getFileContent() != null) {
                        existingMemberFiles.setFileContent(memberFilesDTO.getFileContent());
                    }
                    if (memberFilesDTO.getFileContentContentType() != null) {
                        existingMemberFiles.setFileContentContentType(memberFilesDTO.getFileContentContentType());
                    }

                    return existingMemberFiles;
                }
            )
            .map(memberFilesRepository::save)
            .map(
                savedMemberFiles -> {
                    memberFilesSearchRepository.save(savedMemberFiles);

                    return savedMemberFiles;
                }
            )
            .map(memberFilesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberFilesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MemberFiles");
        return memberFilesRepository.findAll(pageable).map(memberFilesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberFilesDTO> findOne(Long id) {
        log.debug("Request to get MemberFiles : {}", id);
        return memberFilesRepository.findById(id).map(memberFilesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MemberFiles : {}", id);
        memberFilesRepository.deleteById(id);
        memberFilesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberFilesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MemberFiles for query {}", query);
        return memberFilesSearchRepository.search(queryStringQuery(query), pageable).map(memberFilesMapper::toDto);
    }
}
