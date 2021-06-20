package org.emu.membership.service.mapper;

import org.emu.membership.domain.*;
import org.emu.membership.service.dto.MemberFilesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MemberFiles} and its DTO {@link MemberFilesDTO}.
 */
@Mapper(componentModel = "spring", uses = { MemberMapper.class })
public interface MemberFilesMapper extends EntityMapper<MemberFilesDTO, MemberFiles> {
    @Mapping(target = "member", source = "member", qualifiedByName = "id")
    MemberFilesDTO toDto(MemberFiles s);
}
