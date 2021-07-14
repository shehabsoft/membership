package org.emu.membership.integration.events.handlers;

import java.util.Objects;
import java.util.function.Consumer;
import org.camunda.spin.plugin.variable.SpinValues;
import org.emu.common.events.AbstractEvent;
import org.emu.common.events.MemberEvent;
import org.emu.common.status.MemberApprovalStatus;
import org.emu.common.status.MemberStatus;
import org.emu.common.status.NotificationStatus;
import org.emu.membership.domain.Member;
import org.emu.membership.integration.events.publishers.MemberStatusPublisher;
import org.emu.membership.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberEventHandler {
    //
    //    @Autowired
    //    private ProcessEngine camunda;
    //
    //    @Autowired
    //    CamundaMicroServiceProxy camundaMicroServiceProxy;
    //
    //    @Autowired
    //    private MemberRepository repository;
    //
    //    @Autowired
    //    private MemberStatusPublisher publisher;
    //
    //    private final Logger log = LoggerFactory.getLogger(MemberEventHandler.class);
    //
    //    @Transactional
    //    public void updateMember(final long id, Consumer<Member> consumer) {
    //        this.repository.findById(id).ifPresent(consumer.andThen(this::updateMember));
    //    }
    //
    //    private void updateMember(Member member) {
    //        if (Objects.isNull(member.getNotificationStatus()) || Objects.isNull(member.getMemberApprovalStatus())) return;
    //        //
    //        var isComplete =
    //            MemberApprovalStatus.APPROVED.equals(member.getMemberApprovalStatus()) &&
    //            NotificationStatus.SENT.equals(member.getNotificationStatus());
    //        //
    //        var orderStatus = isComplete ? MemberStatus.COMPLETED : MemberStatus.REJECTED;
    //        member.setMemberStatus(orderStatus);
    //
    //        if (!isComplete) {
    //            this.publisher.raiseMemberEvent(member, orderStatus);
    //        }
    //        //According to business case you can implement SAGA by rollback
    //
    //    }
    //
    //    //  @Autowired
    //    //  private ProcessEngine camunda;
    //
    //    @Transactional
    //    public void fireMemberProcess(MemberEvent me) {
    //        fireMemberProcess1(me);
    //    }
    //
    //    public void correlatingProcessInstances(AbstractEvent message) {
    //        long correlatingInstances = camunda
    //            .getRuntimeService()
    //            .createExecutionQuery() //
    //            .messageEventSubscriptionName(message.getType()) //
    //            .processInstanceBusinessKey(message.getTraceid()) //
    //            .count();
    //        //
    //        if (correlatingInstances == 1) {
    //            //
    //            log.error("Correlating " + message + " to waiting flow instance");
    //            //
    //            camunda
    //                .getRuntimeService()
    //                .createMessageCorrelation(message.getType())
    //                .processInstanceBusinessKey(message.getTraceid())
    //                .setVariable( //
    //                    "PAYLOAD_" + message.getType(), //
    //                    SpinValues.jsonValue(message.getData().toString()).create()
    //                ) //
    //                .correlateWithResult();
    //        }
    //    }
    //
    //    @Transactional
    //    public void fireMemberProcess1(MemberEvent me) {
    //        log.error(me.toString());
    //        log.error("New member added, start flow:" + me.getData().getId());
    //        log.error("Event Type:" + me.getType());
    //        // and kick of a new flow instance
    //        MessageCorrelationResult s = camunda
    //            .getRuntimeService()
    //            .createMessageCorrelation(me.getType())
    //            .processInstanceBusinessKey(me.getTraceid())
    //            .setVariable("memberId", me.getData().getId() + "")
    //            .correlateWithResult();
    //        //
    //        log.error(s.toString());
    //    }
}
