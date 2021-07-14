package org.emu.membership.service.mapper;

import org.emu.membership.domain.*;
import org.emu.membership.service.dto.MembershipStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipStatus} and its DTO {@link MembershipStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MembershipStatusMapper extends EntityMapper<MembershipStatusDTO, MembershipStatus> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MembershipStatusDTO toDtoName(MembershipStatus membershipStatus);
}
