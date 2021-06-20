package org.emu.membership.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipCategory.class);
        MembershipCategory membershipCategory1 = new MembershipCategory();
        membershipCategory1.setId(1L);
        MembershipCategory membershipCategory2 = new MembershipCategory();
        membershipCategory2.setId(membershipCategory1.getId());
        assertThat(membershipCategory1).isEqualTo(membershipCategory2);
        membershipCategory2.setId(2L);
        assertThat(membershipCategory1).isNotEqualTo(membershipCategory2);
        membershipCategory1.setId(null);
        assertThat(membershipCategory1).isNotEqualTo(membershipCategory2);
    }
}
