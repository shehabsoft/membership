package org.emu.membership.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembershipTypeMapperTest {

    private MembershipTypeMapper membershipTypeMapper;

    @BeforeEach
    public void setUp() {
        membershipTypeMapper = new MembershipTypeMapperImpl();
    }
}
