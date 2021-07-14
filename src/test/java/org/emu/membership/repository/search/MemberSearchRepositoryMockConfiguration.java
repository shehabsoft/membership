package org.emu.membership.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MemberSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MemberSearchRepositoryMockConfiguration {

    @MockBean
    private MemberSearchRepository mockMemberSearchRepository;
}
