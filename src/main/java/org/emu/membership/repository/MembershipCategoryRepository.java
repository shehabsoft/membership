package org.emu.membership.repository;

import org.emu.membership.domain.MembershipCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MembershipCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipCategoryRepository extends JpaRepository<MembershipCategory, Long> {}
