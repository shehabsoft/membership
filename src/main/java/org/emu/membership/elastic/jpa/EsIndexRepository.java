package org.emu.membership.elastic.jpa;

import org.emu.membership.elastic.domain.EsIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EsIndex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsIndexRepository extends JpaRepository<EsIndex, Long> {
}
