package org.emu.membership.repository.search;

import org.emu.membership.domain.MembershipCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MembershipCategory} entity.
 */
public interface MembershipCategorySearchRepository extends ElasticsearchRepository<MembershipCategory, Long> {}
