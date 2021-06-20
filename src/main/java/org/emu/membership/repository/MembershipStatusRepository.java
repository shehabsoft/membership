package org.emu.membership.repository;

import org.emu.membership.domain.MembershipStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MembershipStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipStatusRepository extends JpaRepository<MembershipStatus, Long> {}
