package org.emu.membership.integration.feign;

import java.util.List;
import org.emu.membership.client.AuthorizedFeignClient;
import org.emu.membership.domain.Member;
import org.emu.membership.service.dto.StpMessageDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@AuthorizedFeignClient(name = "ruleEngineProxy")
public interface RuleEngineProxy {
    @GetMapping("/api/printHello")
    String printHello();

    @PostMapping(value = "/api/validate-member")
    List<StpMessageDTO> validateMember(@RequestBody Member member) throws Exception;

    @PostMapping(value = "/api/validate-dto")
    List<StpMessageDTO> validateDto(@RequestBody Object request, @RequestParam String simpleClassName) throws Exception;
}
