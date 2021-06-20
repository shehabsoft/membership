package org.emu.membership.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipStatusDTO.class);
        MembershipStatusDTO membershipStatusDTO1 = new MembershipStatusDTO();
        membershipStatusDTO1.setId(1L);
        MembershipStatusDTO membershipStatusDTO2 = new MembershipStatusDTO();
        assertThat(membershipStatusDTO1).isNotEqualTo(membershipStatusDTO2);
        membershipStatusDTO2.setId(membershipStatusDTO1.getId());
        assertThat(membershipStatusDTO1).isEqualTo(membershipStatusDTO2);
        membershipStatusDTO2.setId(2L);
        assertThat(membershipStatusDTO1).isNotEqualTo(membershipStatusDTO2);
        membershipStatusDTO1.setId(null);
        assertThat(membershipStatusDTO1).isNotEqualTo(membershipStatusDTO2);
    }
}
