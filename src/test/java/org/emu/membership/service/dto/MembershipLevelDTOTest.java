package org.emu.membership.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipLevelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipLevelDTO.class);
        MembershipLevelDTO membershipLevelDTO1 = new MembershipLevelDTO();
        membershipLevelDTO1.setId(1L);
        MembershipLevelDTO membershipLevelDTO2 = new MembershipLevelDTO();
        assertThat(membershipLevelDTO1).isNotEqualTo(membershipLevelDTO2);
        membershipLevelDTO2.setId(membershipLevelDTO1.getId());
        assertThat(membershipLevelDTO1).isEqualTo(membershipLevelDTO2);
        membershipLevelDTO2.setId(2L);
        assertThat(membershipLevelDTO1).isNotEqualTo(membershipLevelDTO2);
        membershipLevelDTO1.setId(null);
        assertThat(membershipLevelDTO1).isNotEqualTo(membershipLevelDTO2);
    }
}
