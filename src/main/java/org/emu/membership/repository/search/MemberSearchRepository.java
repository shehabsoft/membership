package org.emu.membership.repository.search;

import java.util.List;
import org.emu.membership.domain.Member;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Member} entity.
 */
public interface MemberSearchRepository extends ElasticsearchRepository<Member, Long> {
    List<Member> findByFirstNameContaining(String key);
}
