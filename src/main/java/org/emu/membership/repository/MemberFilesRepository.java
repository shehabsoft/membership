package org.emu.membership.repository;

import org.emu.membership.domain.MemberFiles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MemberFiles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberFilesRepository extends JpaRepository<MemberFiles, Long> {}
