package org.emu.membership.repository.search;

import org.emu.membership.domain.MembershipLevel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MembershipLevel} entity.
 */
public interface MembershipLevelSearchRepository extends ElasticsearchRepository<MembershipLevel, Long> {}
