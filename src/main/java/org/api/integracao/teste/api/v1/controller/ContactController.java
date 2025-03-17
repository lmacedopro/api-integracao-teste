package org.api.integracao.teste.api.v1.controller;

import java.util.Map;

import org.api.integracao.teste.api.v1.records.request.Contact;
import org.api.integracao.teste.resources.client.hubspot.api.service.ApiClientService;
import org.api.integracao.teste.resources.client.hubspot.api.service.NotificationConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("contacts")
public class ContactController {
	
	private final Logger logger = LoggerFactory.getLogger(ContactController.class);
	
	private final ApiClientService apiClientService;
	private final NotificationConfigService notificationConfigService;
	
	public ContactController(ApiClientService apiClientService, NotificationConfigService notificationConfigService) {
		this.apiClientService = apiClientService;
		this.notificationConfigService = notificationConfigService;
	}
    
    @RateLimiter(name = "contactsRateLimiter")
    @PostMapping("create")
    public Mono<ResponseEntity<Map<String, Object>>> createContact(@RequestBody Contact data) {

        return apiClientService.sendPostRequest("/crm/v3/objects/contacts", data)
            .map(response -> {
            	
            	if (response.containsValue("error")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            	
            	return ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            });
    }
    
    @PostMapping("/notification")
    public ResponseEntity<Object> testeWebhook(
    	@RequestHeader("X-HubSpot-Signature") String signature,
    	@RequestBody String notification){
    	
    	if(!notificationConfigService.validateSignature(signature,notification)) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"A validação de assinatura falhou. Requisição rejeitada.\"}");
    	}
    	
    	/*
    	 * Para fins de teste, o processamento do Webhook é apenas um print em tela usando um logger.
    	 * 
    	 * Existem várias de processar o retorno de um Webhook, como persistir em base, ou enviar a uma Fila de mensageria.
    	 * Seja qual for a implementação, o processamento pode ser delegado a um serviço interno.
    	 *
    	 */
    	logger.info("========== NOTIFICACAO DE CRIACAO DE CONTATOS =======");
    	logger.info("Um contato foi criado em sua conta HubSpot com os seguintes dados");
    	logger.info(notification);
    	logger.info("=====================================================");
    	
    	return ResponseEntity.ok().build();
    }

    
    
}
