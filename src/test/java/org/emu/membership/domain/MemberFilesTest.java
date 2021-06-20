package org.emu.membership.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.emu.membership.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberFilesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberFiles.class);
        MemberFiles memberFiles1 = new MemberFiles();
        memberFiles1.setId(1L);
        MemberFiles memberFiles2 = new MemberFiles();
        memberFiles2.setId(memberFiles1.getId());
        assertThat(memberFiles1).isEqualTo(memberFiles2);
        memberFiles2.setId(2L);
        assertThat(memberFiles1).isNotEqualTo(memberFiles2);
        memberFiles1.setId(null);
        assertThat(memberFiles1).isNotEqualTo(memberFiles2);
    }
}
