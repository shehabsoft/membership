package org.emu.membership.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MembershipStatusSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MembershipStatusSearchRepositoryMockConfiguration {

    @MockBean
    private MembershipStatusSearchRepository mockMembershipStatusSearchRepository;
}
