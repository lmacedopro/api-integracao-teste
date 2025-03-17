package org.api.integracao.teste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiIntegracaoTesteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiIntegracaoTesteApplication.class, args);
	}

}
