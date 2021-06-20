package org.emu.membership.integration.feign;

import org.emu.common.events.MemberEvent;
import org.emu.membership.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright 2021-2022 By Dirac Systems.
 *
 * Created by {@khalid.nouh on 24/3/2021}.
 */

@AuthorizedFeignClient(name = "bpmProxy")
public interface BpmProxy {
    @PostMapping("/api/startProcessByMessageEvent")
    void raiseMemberProcess(@RequestBody MemberEvent me);
}
