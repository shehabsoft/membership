package org.emu.membership.service.mapper;

import org.emu.membership.domain.*;
import org.emu.membership.service.dto.MembershipCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipCategory} and its DTO {@link MembershipCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MembershipCategoryMapper extends EntityMapper<MembershipCategoryDTO, MembershipCategory> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MembershipCategoryDTO toDtoName(MembershipCategory membershipCategory);
}
