package org.emu.membership.service.mapper;

import org.emu.membership.domain.*;
import org.emu.membership.service.dto.MembershipLevelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipLevel} and its DTO {@link MembershipLevelDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MembershipLevelMapper extends EntityMapper<MembershipLevelDTO, MembershipLevel> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MembershipLevelDTO toDtoName(MembershipLevel membershipLevel);
}
