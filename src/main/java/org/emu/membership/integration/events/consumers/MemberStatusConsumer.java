package org.emu.membership.integration.events.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Consumer;
import org.emu.common.dto.MemberDto;
import org.emu.common.dto.MemberStatus;
import org.emu.common.dto.bpm.EVENTSTYPES;
import org.emu.common.events.MemberApprovalEvent;
import org.emu.common.events.MemberEvent;
import org.emu.common.events.NotificationEvent;
import org.emu.common.status.MemberApprovalStatus;
import org.emu.membership.integration.events.handlers.MemberEventHandler;
import org.emu.membership.integration.events.publishers.MemberStatusPublisher;
import org.emu.membership.integration.feign.BpmProxy;
import org.emu.membership.service.MemberService;
import org.emu.membership.service.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberStatusConsumer {

    private final Logger log = LoggerFactory.getLogger(MemberStatusConsumer.class);

    @Autowired
    private MemberEventHandler orderEventHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BpmProxy bpmProxy;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberStatusPublisher memberStatusPublisher;

    @Bean
    public Consumer<MemberEvent> memberEventConsumer() {
        return me -> {
            log.debug("Inside memberEventConsumer");

            if (me.getStatus() != null && me.getStatus().toString().equals("REJECTED")) {
                MemberDto memberDto = objectMapper.convertValue(me.getGenericDto(), MemberDto.class);

                memberService.delete(memberDto.getId());
            }
        };
    }

    @Bean
    public Consumer<MemberApprovalEvent> memberApprovalEventConsumer() {
        return pe -> {
            if (pe.getStatus() != null && pe.getStatus().toString().equals("APPROVED")) {
                MemberDto memberDto = objectMapper.convertValue(pe.getGenericDto(), MemberDto.class);
                MemberDTO memberDTO = objectMapper.convertValue(memberDto, MemberDTO.class);
                MemberDTO save = memberService.save(memberDTO);
                memberDto.setId(save.getId());
            }
        };
    }
}
