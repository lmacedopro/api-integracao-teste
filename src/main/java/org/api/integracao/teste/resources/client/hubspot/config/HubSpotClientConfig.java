package org.api.integracao.teste.resources.client.hubspot.config;

import org.api.integracao.teste.resources.client.hubspot.config.properties.HubSpotProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HubSpotClientConfig {
	
	private final HubSpotProperties hubSpotProperties;
	
	public HubSpotClientConfig(HubSpotProperties hubSpotProperties) {
		this.hubSpotProperties = hubSpotProperties;
	}
	
	@Bean
    WebClient hubApiClient() {
        return WebClient.builder()
                .baseUrl(hubSpotProperties.apiBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
	
	@Bean
	WebClient hubSpotTokenClient() {
		return WebClient.builder()
			.baseUrl(hubSpotProperties.apiTokenUri())
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
	}
}
