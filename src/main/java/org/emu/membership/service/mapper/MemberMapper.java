package org.emu.membership.service.mapper;

import org.emu.membership.domain.*;
import org.emu.membership.service.dto.MemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { MembershipStatusMapper.class, MembershipCategoryMapper.class, MembershipTypeMapper.class, MembershipLevelMapper.class }
)
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "membershipStatus", source = "membershipStatus", qualifiedByName = "name")
    @Mapping(target = "membershipCategory", source = "membershipCategory", qualifiedByName = "name")
    @Mapping(target = "membershipType", source = "membershipType", qualifiedByName = "name")
    @Mapping(target = "membershipLevel", source = "membershipLevel", qualifiedByName = "name")
    MemberDTO toDto(Member s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoId(Member member);
}
