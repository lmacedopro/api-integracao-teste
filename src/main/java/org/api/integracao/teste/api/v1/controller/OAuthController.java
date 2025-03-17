package org.api.integracao.teste.api.v1.controller;

import java.util.Map;

import org.api.integracao.teste.api.v1.exception.ApiException;
import org.api.integracao.teste.resources.client.hubspot.api.service.OAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("oauth")
public class OAuthController {
	
	private static final String ERROR_KEY = "error";

	private final OAuthService oauthService;
	
	public OAuthController(OAuthService oauthService) {
		this.oauthService = oauthService;
	}
	
	@GetMapping("/code/url")
    public Mono<ResponseEntity<Map<String, Object>>> getAuthUrl(){
    	
		return oauthService.generateOauth2Uri()
			.map(response -> {
            	
            	if (response.containsKey(ERROR_KEY)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            	
            	return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            });
    }
	
	@GetMapping("/callback")
	public Mono<String> handleOAuthCallback(@RequestParam String code) {
	    return oauthService.exchangeAuthCodeForTokens(code)
	        .flatMap(tokens -> {
	            if (tokens.containsKey(ERROR_KEY)) {
	                return Mono.error(new ApiException("Erro ao obter access_token: " + tokens.get(ERROR_KEY)));
	            }

	            String accessToken = tokens.get("access_token").toString();
	            return Mono.just(generateCallBackResponse(accessToken));
	        });
	}
	
	@GetMapping("/token/refresh")
    public Mono<ResponseEntity<Map<String,Object>>> getRefreshToken(){
    	return oauthService.getRefreshToken()
                .map(tokens -> {
                    if (tokens.containsKey(ERROR_KEY)) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tokens);
                    }

                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(tokens);
                });
    }
	
	private String generateCallBackResponse(String accessToken) {
		
		StringBuilder htmlResponseBuilder = new StringBuilder();

		htmlResponseBuilder.append("<!DOCTYPE html>")
		    .append("<html>")
		    .append("<head><title>Fechar Guia</title></head>")
		    .append("<body>")
		    .append("<h1>API INTEGRACAO TESTE</h1>")
		    .append("<h4>Código de Autorização processado com Sucesso!</h4>")
		    .append("<h4>Você está Autenticado no HubSpot!</h4>")
		    .append("<p>&nbsp;</p>")
		    .append("<p><strong>Access Token: </strong><small>").append(accessToken).append("</small></p>")
		    .append("<p>&nbsp;</p>")
		    .append("<p><strong><small>Por questões de segurança esta aba só poderá ser fechada manualmente.</small></stong></p>")
		    .append("</body>")
		    .append("</html>");

		return htmlResponseBuilder.toString();
	}
}
