package org.api.integracao.teste.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.base")
public record AppConfigProperties(
	String url,
	String ngrokUrl		
) {}
