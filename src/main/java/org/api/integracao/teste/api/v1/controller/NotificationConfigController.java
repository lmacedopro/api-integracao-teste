package org.api.integracao.teste.api.v1.controller;

import java.util.Map;

import org.api.integracao.teste.resources.client.hubspot.api.service.NotificationConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("config")
public class NotificationConfigController {
	
	private final NotificationConfigService notificationConfigservice;
	
	public NotificationConfigController(NotificationConfigService notificationConfigservice) {
		this.notificationConfigservice = notificationConfigservice;
	}
	
	@GetMapping("notification/contacts")
    public Mono<ResponseEntity<Map<String, Object>>> configWebhookUrl(){
    	
    	return notificationConfigservice.configContactsNotification()
    	.map(response -> {
        	
        	if (response.containsKey("error")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        	
        	return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        });
    }
}
