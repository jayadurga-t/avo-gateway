package com.avo.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator avoBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder){

		return routeLocatorBuilder.routes()
				.route(new Function<PredicateSpec, Buildable<Route>>() {
							@Override
							public Buildable<Route> apply(PredicateSpec predicateSpec) {
								return predicateSpec.path("/avobank/accounts/**")
													.filters(new Function<GatewayFilterSpec, UriSpec>() {
														@Override
														public UriSpec apply(GatewayFilterSpec gatewayFilterSpec) {
															return gatewayFilterSpec.rewritePath("/avobank/accounts/(?<segment>.*)","/${segment}")
																					.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
//																					.circuitBreaker(new Consumer<SpringCloudCircuitBreakerFilterFactory.Config>() {
//																						@Override
//																						public void accept(SpringCloudCircuitBreakerFilterFactory.Config config) {
//																							config.setName("accountsCircuitBreaker").setFallbackUri("forward:/contactSupport");
//																						}
//																					});
																					.retry(new Consumer<RetryGatewayFilterFactory.RetryConfig>() {
																						@Override
																						public void accept(RetryGatewayFilterFactory.RetryConfig retryConfig) {
																							retryConfig.setRetries(3)
																										.setMethods(HttpMethod.GET)
																										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true);
																						}
																					});
														}
													})
													.uri("lb://ACCOUNTS");
							}
						}
				)
				.route(new Function<PredicateSpec, Buildable<Route>>() {
							@Override
							public Buildable<Route> apply(PredicateSpec predicateSpec) {
								return predicateSpec.path("/avobank/cards/**")
													.filters(new Function<GatewayFilterSpec, UriSpec>() {
														@Override
														public UriSpec apply(GatewayFilterSpec gatewayFilterSpec) {
															return gatewayFilterSpec.rewritePath("/avobank/cards/(?<segment>.*)","/${segment}")
																					.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
//																					.circuitBreaker(new Consumer<SpringCloudCircuitBreakerFilterFactory.Config>() {
//																						@Override
//																						public void accept(SpringCloudCircuitBreakerFilterFactory.Config config) {
//																							config.setName("cardsCircuitBreaker").setFallbackUri("forward:/contactSupport");
//																						}
//																					});
																					.retry(new Consumer<RetryGatewayFilterFactory.RetryConfig>() {
																						@Override
																						public void accept(RetryGatewayFilterFactory.RetryConfig retryConfig) {
																							retryConfig.setRetries(3)
																										.setMethods(HttpMethod.GET)
																										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true);
																						}
																					});
														}
													})
													.uri("lb://CARDS");
							}
						}
				)
				.route(new Function<PredicateSpec, Buildable<Route>>() {
							@Override
							public Buildable<Route> apply(PredicateSpec predicateSpec) {
								return predicateSpec.path("/avobank/loans/**")
													.filters(new Function<GatewayFilterSpec, UriSpec>() {
														@Override
														public UriSpec apply(GatewayFilterSpec gatewayFilterSpec) {
															return gatewayFilterSpec.rewritePath("/avobank/loans/(?<segment>.*)","/${segment}")
																					.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
//																					.circuitBreaker(new Consumer<SpringCloudCircuitBreakerFilterFactory.Config>() {
//																						@Override
//																						public void accept(SpringCloudCircuitBreakerFilterFactory.Config config) {
//																							config.setName("loansCircuitBreaker").setFallbackUri("forward:/contactSupport");
//																						}
//																					});
																					.retry(new Consumer<RetryGatewayFilterFactory.RetryConfig>() {
																						@Override
																						public void accept(RetryGatewayFilterFactory.RetryConfig retryConfig) {
																							retryConfig.setRetries(3)
																										.setMethods(HttpMethod.GET)
																										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true);
																						}
																					});
														}
													})
													.uri("lb://LOANS");
							}
						}
				)
				.build();


	}

}
