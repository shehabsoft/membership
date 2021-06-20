package org.emu.membership.repository;

import org.emu.membership.domain.MembershipLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MembershipLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipLevelRepository extends JpaRepository<MembershipLevel, Long> {}
