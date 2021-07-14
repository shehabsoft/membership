package org.emu.membership.elastic.jpa;


import org.emu.membership.elastic.domain.EsField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EsField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsFieldRepository extends JpaRepository<EsField, Long> {
}
