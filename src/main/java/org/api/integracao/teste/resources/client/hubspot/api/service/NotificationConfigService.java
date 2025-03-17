package org.api.integracao.teste.resources.client.hubspot.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.api.integracao.teste.config.AppConfigProperties;
import org.api.integracao.teste.resources.client.hubspot.config.properties.HubSpotProperties;
import org.api.integracao.teste.resources.client.records.ConfigNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class NotificationConfigService {
	
	private final Logger logger = LoggerFactory.getLogger(NotificationConfigService.class);
	
	private final WebClient hubApiClient;
	private final HubSpotProperties hubSpotProperties;
	private final AppConfigProperties appConfigProperties;
	
	public NotificationConfigService(WebClient hubApiClient, HubSpotProperties hubSpotProperties, AppConfigProperties appConfigProperties) {
		this.hubApiClient = hubApiClient;
		this.hubSpotProperties = hubSpotProperties;
		this.appConfigProperties = appConfigProperties;
	}

	public Mono<Map<String, Object>> configContactsNotification() {
    	
    	ConfigNotification requestBody = new ConfigNotification(appConfigProperties.ngrokUrl() + "/contacts/notification");

        return hubApiClient.put()
            .uri(hubSpotProperties.notificationConfigUrl())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorResume(WebClientResponseException.class, e -> {
                	
                logger.error("Erro ao realizar uma requisicao PUT de configuracao - Status: {} - Erro: {}", e.getStatusCode(), e.getResponseBodyAsString());
                return Mono.just(e.getResponseBodyAs(new ParameterizedTypeReference<Map<String, Object>>() {}));
            });
    }
    
    public boolean validateSignature(String signature, String notification) {
    	
    	String input = hubSpotProperties.oauth2ClientSecret() + notification;
    	return generateSHA256(input).equalsIgnoreCase(signature);
    }
    
    
    
    public static String generateSHA256(String input) {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256", e);
        }
    }
}
