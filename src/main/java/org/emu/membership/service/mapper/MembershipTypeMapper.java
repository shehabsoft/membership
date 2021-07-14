package org.emu.membership.service.mapper;

import org.emu.membership.domain.*;
import org.emu.membership.service.dto.MembershipTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipType} and its DTO {@link MembershipTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MembershipTypeMapper extends EntityMapper<MembershipTypeDTO, MembershipType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MembershipTypeDTO toDtoName(MembershipType membershipType);
}
