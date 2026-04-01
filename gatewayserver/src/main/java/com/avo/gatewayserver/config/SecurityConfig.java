package com.avo.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.authorizeExchange(new Customizer<ServerHttpSecurity.AuthorizeExchangeSpec>() {
            @Override
            public void customize(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
                authorizeExchangeSpec.pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/avobank/accounts/**").hasRole("ACCOUNTS")
                        .pathMatchers("avobank/cards/**").hasRole("CARDS")
                        .pathMatchers("/avobank/loans/**").hasRole("LOANS");
            }
        })
                .oauth2ResourceServer(new Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec>() {
                    @Override
                    public void customize(ServerHttpSecurity.OAuth2ResourceServerSpec oAuth2ResourceServerSpec) {
                        oAuth2ResourceServerSpec.jwt(new Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec>() {
                            @Override
                            public void customize(ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwtSpec) {
                                jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor());
                            }
                        });
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

    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor(){
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
