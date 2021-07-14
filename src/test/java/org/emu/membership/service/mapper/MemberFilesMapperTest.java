package org.emu.membership.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberFilesMapperTest {

    private MemberFilesMapper memberFilesMapper;

    @BeforeEach
    public void setUp() {
        memberFilesMapper = new MemberFilesMapperImpl();
    }
}
