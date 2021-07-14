package org.emu.membership.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipType.class);
        MembershipType membershipType1 = new MembershipType();
        membershipType1.setId(1L);
        MembershipType membershipType2 = new MembershipType();
        membershipType2.setId(membershipType1.getId());
        assertThat(membershipType1).isEqualTo(membershipType2);
        membershipType2.setId(2L);
        assertThat(membershipType1).isNotEqualTo(membershipType2);
        membershipType1.setId(null);
        assertThat(membershipType1).isNotEqualTo(membershipType2);
    }
}
