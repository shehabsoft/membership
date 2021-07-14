package org.emu.membership.repository.search;

import org.emu.membership.domain.MemberFiles;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MemberFiles} entity.
 */
public interface MemberFilesSearchRepository extends ElasticsearchRepository<MemberFiles, Long> {}
