package org.emu.membership.repository.search;

import org.emu.membership.domain.MembershipType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MembershipType} entity.
 */
public interface MembershipTypeSearchRepository extends ElasticsearchRepository<MembershipType, Long> {}
