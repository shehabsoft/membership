package org.emu.membership.client;

import feign.RequestInterceptor;
import org.emu.membership.security.oauth2.AuthorizationHeaderUtil;
import org.springframework.context.annotation.Bean;

public class OAuth2InterceptedFeignConfiguration {

    @Bean(name = "oauth2RequestInterceptor")
    public RequestInterceptor getOAuth2RequestInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil) {
        return new TokenRelayRequestInterceptor(authorizationHeaderUtil);
    }
}
