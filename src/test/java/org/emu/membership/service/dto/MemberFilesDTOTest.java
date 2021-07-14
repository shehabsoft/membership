package org.emu.membership.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberFilesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberFilesDTO.class);
        MemberFilesDTO memberFilesDTO1 = new MemberFilesDTO();
        memberFilesDTO1.setId(1L);
        MemberFilesDTO memberFilesDTO2 = new MemberFilesDTO();
        assertThat(memberFilesDTO1).isNotEqualTo(memberFilesDTO2);
        memberFilesDTO2.setId(memberFilesDTO1.getId());
        assertThat(memberFilesDTO1).isEqualTo(memberFilesDTO2);
        memberFilesDTO2.setId(2L);
        assertThat(memberFilesDTO1).isNotEqualTo(memberFilesDTO2);
        memberFilesDTO1.setId(null);
        assertThat(memberFilesDTO1).isNotEqualTo(memberFilesDTO2);
    }
}
