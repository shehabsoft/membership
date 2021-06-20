package org.emu.membership.repository;

import org.emu.membership.domain.MembershipType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MembershipType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipTypeRepository extends JpaRepository<MembershipType, Long> {}
