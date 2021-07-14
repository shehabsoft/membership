package org.emu.membership.integration.flow;

import org.emu.common.status.MemberApprovalStatus;
import org.emu.membership.domain.Member;
import org.emu.membership.integration.events.publishers.MemberStatusPublisher;
import org.emu.membership.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class MemberApprovalAdapter { //implements JavaDelegate {
    //
    //    @Autowired
    //    private MemberStatusPublisher memberStatusPublisher;
    //
    //    @Autowired
    //    private MemberRepository memberRepository;
    //
    //    private final Logger log = LoggerFactory.getLogger(SendNotificationAdapter.class);
    //
    //    @Override
    //    public void execute(DelegateExecution context) throws Exception {
    //        Object approved = context.getVariable("approved");
    //        log.error("Member Approved=" + approved);
    //        //
    //        Long id = Long.parseLong((String) context.getVariable("memberId"));
    //        //
    //        log.error("Member Id=" + id);
    //        //
    //        Member member = memberRepository.findById(id).get();
    //        //
    //        MemberApprovalStatus s = ((Boolean) approved) ? MemberApprovalStatus.APPROVED : MemberApprovalStatus.REJECTED;
    //
    //        memberStatusPublisher.raiseMemberApprovalEvent(member, s);
    //    }
}
