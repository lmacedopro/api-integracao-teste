package org.api.integracao.teste.resources.client.hubspot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hubspot.config")
public record HubSpotProperties(
	String oauth2Scopes,
	String oauth2ClientId,
	String oauth2ClientSecret,
	String oauth2Url,
	String oauth2RedirectUri,
	String oauth2TokenUrl,
	String apiBaseUrl,
	String apiTokenUri,
	String devApiKey,
	Long appId,
	String notificationConfigUrl		
) {}
