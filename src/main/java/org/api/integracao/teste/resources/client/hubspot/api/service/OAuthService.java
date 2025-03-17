package org.api.integracao.teste.resources.client.hubspot.api.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.api.integracao.teste.resources.client.hubspot.config.properties.HubSpotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class OAuthService {
	
	private final Logger logger = LoggerFactory.getLogger(OAuthService.class);
	
	private final WebClient hubSpotTokenClient;
    private final HubSpotProperties hubSpotProperties;
    private Map<String, Object> accessToken;

    public OAuthService(WebClient hubSpotTokenClient, HubSpotProperties hubSpotProperties) {
        this.hubSpotTokenClient = hubSpotTokenClient;
        this.hubSpotProperties = hubSpotProperties;
    }
	
	public Mono<Map<String,Object>> generateOauth2Uri() {

       String formattedScopes = formatScopes(hubSpotProperties.oauth2Scopes());
       
       Map<String,Object> data = new HashMap<>();
       data.put("authorizationCode", String.format("%s?client_id=%s&scope=%s&redirect_uri=%s",
	    		hubSpotProperties.oauth2Url(),
	    		hubSpotProperties.oauth2ClientId(),
	    		formattedScopes,
	    		hubSpotProperties.oauth2RedirectUri()));
       return Mono.just(data);

    }
	    
    public Mono<Map<String, Object>> exchangeAuthCodeForTokens(String code) {
        if (code == null) {
        	logger.error("Código de autorização não pode ser nulo.");
            return Mono.error(new IllegalArgumentException("Código de autorização não pode ser nulo."));
        }

        MultiValueMap<String, String> body = createAuthCodeRequest(code);

        return hubSpotTokenClient.post()
            .body(BodyInserters.fromFormData(body))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorResume(WebClientResponseException.class, e -> {
            	
            	logger.error("Erro ao requisitar um token de acesso - Status: {} - Erro: {}", e.getStatusCode(), e.getResponseBodyAsString());
                
                return Mono.just(e.getResponseBodyAs(new ParameterizedTypeReference<Map<String, Object>>() {}));
            })
            .doOnSuccess(this::setAccessToken);
    }
	    
	    
	    
    public Map<String,Object> getAccessToken(){
    	return this.accessToken;
    }
	    
    public Mono<Map<String, Object>> getRefreshToken() {
        if (this.accessToken == null) {
            return Mono.error(new IllegalArgumentException("Refresh token cannot be null"));
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", hubSpotProperties.oauth2ClientId());
        body.add("client_secret", hubSpotProperties.oauth2ClientSecret());
        body.add("refresh_token", this.accessToken.get("refresh_token").toString());

        return hubSpotTokenClient.post()
            .body(BodyInserters.fromFormData(body))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorResume(WebClientResponseException.class, e -> {
            	
            	logger.error("Erro ao requisitar um token de acesso - Status: {} - Erro: {}", e.getStatusCode(), e.getResponseBodyAsString());
                
                return Mono.just(e.getResponseBodyAs(new ParameterizedTypeReference<Map<String, Object>>() {}));
                
            }).doOnSuccess(this::setAccessToken);
    }
	    
    private String formatScopes(String scopes) {
        return Arrays.stream(scopes.split(","))
                .map(String::trim)
                .collect(Collectors.joining("%20"));
    }
	    
    private MultiValueMap<String, String> createAuthCodeRequest(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", hubSpotProperties.oauth2ClientId());
        body.add("client_secret", hubSpotProperties.oauth2ClientSecret());
        body.add("redirect_uri", hubSpotProperties.oauth2RedirectUri());
        body.add("code", code);
        return body;
    }
	    
    private void setAccessToken(Map<String,Object> token) {
    	this.accessToken = token;
    }

}
