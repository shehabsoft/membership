package org.emu.membership.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipStatus.class);
        MembershipStatus membershipStatus1 = new MembershipStatus();
        membershipStatus1.setId(1L);
        MembershipStatus membershipStatus2 = new MembershipStatus();
        membershipStatus2.setId(membershipStatus1.getId());
        assertThat(membershipStatus1).isEqualTo(membershipStatus2);
        membershipStatus2.setId(2L);
        assertThat(membershipStatus1).isNotEqualTo(membershipStatus2);
        membershipStatus1.setId(null);
        assertThat(membershipStatus1).isNotEqualTo(membershipStatus2);
    }
}
