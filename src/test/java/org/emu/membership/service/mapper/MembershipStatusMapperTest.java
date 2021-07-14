package org.emu.membership.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembershipStatusMapperTest {

    private MembershipStatusMapper membershipStatusMapper;

    @BeforeEach
    public void setUp() {
        membershipStatusMapper = new MembershipStatusMapperImpl();
    }
}
