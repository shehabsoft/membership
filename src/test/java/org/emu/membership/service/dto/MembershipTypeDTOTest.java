package org.emu.membership.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipTypeDTO.class);
        MembershipTypeDTO membershipTypeDTO1 = new MembershipTypeDTO();
        membershipTypeDTO1.setId(1L);
        MembershipTypeDTO membershipTypeDTO2 = new MembershipTypeDTO();
        assertThat(membershipTypeDTO1).isNotEqualTo(membershipTypeDTO2);
        membershipTypeDTO2.setId(membershipTypeDTO1.getId());
        assertThat(membershipTypeDTO1).isEqualTo(membershipTypeDTO2);
        membershipTypeDTO2.setId(2L);
        assertThat(membershipTypeDTO1).isNotEqualTo(membershipTypeDTO2);
        membershipTypeDTO1.setId(null);
        assertThat(membershipTypeDTO1).isNotEqualTo(membershipTypeDTO2);
    }
}
