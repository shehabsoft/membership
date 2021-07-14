package org.emu.membership.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembershipCategoryMapperTest {

    private MembershipCategoryMapper membershipCategoryMapper;

    @BeforeEach
    public void setUp() {
        membershipCategoryMapper = new MembershipCategoryMapperImpl();
    }
}
