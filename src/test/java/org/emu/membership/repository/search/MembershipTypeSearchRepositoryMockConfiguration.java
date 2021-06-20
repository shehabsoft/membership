package org.emu.membership.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MembershipTypeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MembershipTypeSearchRepositoryMockConfiguration {

    @MockBean
    private MembershipTypeSearchRepository mockMembershipTypeSearchRepository;
}
