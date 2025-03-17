package org.api.integracao.teste.resources.client.records;

import java.util.HashMap;
import java.util.Map;

public record ConfigNotification(
	Map<String,Object> throttling, 
	String targetUrl
) {
	
	public ConfigNotification(String targetUrl) {
		this(defaultThrottling(), targetUrl);
	}
	
	private static Map<String, Object> defaultThrottling() {
        Map<String, Object> defaultMap = new HashMap<>();
        defaultMap.put("maxConcurrentRequests", 10);
        return defaultMap;
    }
}
