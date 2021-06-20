package org.emu.membership.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembershipLevelMapperTest {

    private MembershipLevelMapper membershipLevelMapper;

    @BeforeEach
    public void setUp() {
        membershipLevelMapper = new MembershipLevelMapperImpl();
    }
}
