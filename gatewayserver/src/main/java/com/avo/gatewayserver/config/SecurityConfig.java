package com.avo.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.authorizeExchange(new Customizer<ServerHttpSecurity.AuthorizeExchangeSpec>() {
            @Override
            public void customize(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
                authorizeExchangeSpec.pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/avobank/accounts/**").authenticated()
                        .pathMatchers("avobank/cards/**").authenticated()
                        .pathMatchers("/avobank/loans/**").authenticated();
            }
        })
                .oauth2ResourceServer(new Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec>() {
                    @Override
                    public void customize(ServerHttpSecurity.OAuth2ResourceServerSpec oAuth2ResourceServerSpec) {
                        oAuth2ResourceServerSpec.jwt(Customizer.withDefaults());
                    }
                });
        serverHttpSecurity.csrf(new Customizer<ServerHttpSecurity.CsrfSpec>() {
            @Override
            public void customize(ServerHttpSecurity.CsrfSpec csrfSpec) {
                csrfSpec.disable();
            }
        });
        return serverHttpSecurity.build();
    }

}
