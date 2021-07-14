package org.emu.membership.integration.flow;

import org.springframework.stereotype.Component;

@Component
public class SendNotificationAdapter { //implements JavaDelegate {
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
    //        //
    //        Long id = Long.parseLong((String) context.getVariable("memberId"));
    //        //
    //        log.error("Member Id=" + id);
    //        //
    //        Member member = memberRepository.findById(id).get();
    //        //
    //        String traceId = context.getProcessBusinessKey();
    //
    //        memberStatusPublisher.raiseNotificationEvent(member, traceId);
    //    }
}
