package org.emu.membership.repository.search;

import org.emu.membership.domain.MembershipStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MembershipStatus} entity.
 */
public interface MembershipStatusSearchRepository extends ElasticsearchRepository<MembershipStatus, Long> {}
