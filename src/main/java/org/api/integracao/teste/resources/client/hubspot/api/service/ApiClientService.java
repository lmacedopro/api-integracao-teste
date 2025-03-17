package org.api.integracao.teste.resources.client.hubspot.api.service;

import java.util.Map;

import org.api.integracao.teste.api.v1.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class ApiClientService {
	
	private final Logger logger = LoggerFactory.getLogger(ApiClientService.class);
	
	private final WebClient hubApiClient;
	private final OAuthService oauthService;
	
	public ApiClientService(WebClient hubApiClient, OAuthService oauthService) {
		this.hubApiClient = hubApiClient;
		this.oauthService = oauthService;
	}

	public <T> Mono<Map<String, Object>> sendPostRequest(String uri, T requestBody) {
		
	    Map<String, Object> accessTokenMap = oauthService.getAccessToken();
	    
	    if (accessTokenMap == null || !accessTokenMap.containsKey("access_token")) {
	        throw new UnauthorizedException("Usuário não Autenticado! Faça a Autenticação para usar o serviço!");
	    }
	    
        return hubApiClient.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + oauthService.getAccessToken().get("access_token"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestBody))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorResume(WebClientResponseException.class, e -> {
            	
            	logger.error("Erro ao realizar uma requisicao POST - Status: {} - Erro: {}", e.getStatusCode(), e.getResponseBodyAsString());
                return Mono.just(e.getResponseBodyAs(new ParameterizedTypeReference<Map<String, Object>>() {}));
            });
    }
	
	public Mono<Map<String, Object>> sendGetRequest(String uri) {

        return hubApiClient.get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + oauthService.getAccessToken().get("access_token"))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .onErrorResume(WebClientResponseException.class, e -> {
            	
            	logger.error("Erro ao realizar uma requisicao GET - Status: {} - Erro: {}", e.getStatusCode(), e.getResponseBodyAsString());
                return Mono.just(e.getResponseBodyAs(new ParameterizedTypeReference<Map<String, Object>>() {}));
            });
    }
}
