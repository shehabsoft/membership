package org.emu.membership.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipCategoryDTO.class);
        MembershipCategoryDTO membershipCategoryDTO1 = new MembershipCategoryDTO();
        membershipCategoryDTO1.setId(1L);
        MembershipCategoryDTO membershipCategoryDTO2 = new MembershipCategoryDTO();
        assertThat(membershipCategoryDTO1).isNotEqualTo(membershipCategoryDTO2);
        membershipCategoryDTO2.setId(membershipCategoryDTO1.getId());
        assertThat(membershipCategoryDTO1).isEqualTo(membershipCategoryDTO2);
        membershipCategoryDTO2.setId(2L);
        assertThat(membershipCategoryDTO1).isNotEqualTo(membershipCategoryDTO2);
        membershipCategoryDTO1.setId(null);
        assertThat(membershipCategoryDTO1).isNotEqualTo(membershipCategoryDTO2);
    }
}
