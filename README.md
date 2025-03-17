# INTEGRAÇÃO API HUBSPOT 
Este projeto implementa uma API REST em Java para integração com a API do HubSpot. Os principais objetivos incluem:
- Implementar o fluxo OAuth 2.0 (authorization code flow).
- Permitir a criação de contatos no CRM.
- Receber notificações de webhooks relacionadas a eventos de criação de contatos.

A API foi desenvolvida usando o **Spring Boot**. 

### Tecnologias Utilizadas

* [Spring Boot] (https://spring.io/projects/spring-boot)

## Pré Requisitos

* [Java - Versão 17] (https://www.oracle.com/java/technologies/javase/jdk17-0-13-later-archive-downloads.html)
* [Apache Maven] (https://maven.apache.org)

## ✅ Como rodar o projeto

#### 1. **Clone o repositório**
```
git clone <URL_DO_REPOSITORIO>
cd <NOME_DO_REPOSITORIO>
```
#### 2. **Defina as Variáveis de Ambiente**
Crie as seguintes variáveis de ambiente HUBSPOT_CLIENT_ID e HUBSPOT_CLIENT_SECRET no seu sistema operacional definido os valores de client_id e client_secret enviado pelo mantenedor deste repositório:

**Exemplo no Windows**

```
setx HUBSPOT_CLIENT_ID "client_id_enviado"
setx HUBSPOT_CLIENT_SECRET "client_secret_enviado"
```
**Exemplo no Linux**

```
echo "export HUBSPOT_CLIENT_ID=client_id_enviado" >> ~/.bashrc
echo "export HUBSPOT_CLIENT_ID=client_secret_enviado" >> ~/.bashrc
```

#### 3. **Build do projeto**
Rode o comando no terminal (Linux) ou CMD (Windows) para executar build do projeto. Caso não possua um Maven configurado no Sistema Operacional, você pode utilizar a implementação que vem no projeto, subastituindo os comandos **mvn** por **mvnw** nos exemplos a seguir.

```
mvn clean install
```

#### 4. **Execute a aplicação**
Execute o comando para iniciar a aplicação:

```
mvn spring-boot:run
```

Após a aplicação iniciar os logs serão mostrados na janela do terminal (ou CMD) como no exemplo abaixo:

![Aplicacao iniciada com sucesso](https://drive.google.com/uc?export=view&id=1HqiSF1N7GQh7ZYQqBrhP8g6iBLSZMvSI)

## 📌 Informações importantes sobre a aplicação

#### Endpoints
A Api possui os seguintes endpoints:

* **GET oauth/code/url** - Geração da URL OAuth2 para autenticação
* **GET oauth/callback** - Callback do OAuth2 redirecionamento após autenticado
* **POST contacts/create** - Criação de Contatos
* **POST contacts/notification** - Recebe Notificações do Webhook HubSpot
* **GET config/notification/contacts** - Configuracao da URL de Notificação de Contatos.

Você pode baixar a Coleção de endpoints e utilizar para testes no Posman ou Insomnia <aqui>.

#### Configuracao da URL de Notificação

Para testar o endpoint de notificação é necessário expor um endpoint HTTPS para internet, pois é uma exigência nas configurações do Aplicativo Hubspot. Por isso, relatei na seção **Problemas Enfrentados** uma maneira simples de expor a api rodando localmente em um dominio HTTPs utilizando NGROK. Verifique esta seção para mais informações.

## Usando a Api para criar um Contato
Um exemplo simples de como utilizar a api para a criação do contato:
1. envie uma requisição ao endpoint /oauth/code/url. A resposta será um JSON contendo um link de autenticação oauth2 da HubSpot. Caso você não tenha uma conta de desenvolvedor será necessário criá-la. Siga os passos de criação de uma conta de [testes de desenvolvedor](https://br.developers.hubspot.com/docs/getting-started/account-types) na HubSpot.

    **Exemplo de Requisição**
    
    ```
    curl --request GET \
      --url http://localhost:8080/oauth/code/url
      ```
      
    **Exemplo de Resposta**
    
    ```
    {
    	"authorizationCode": "https://app.hubspot.com/oauth/49532047/authorize?client_id=fb40a36a-72f5-4abe-9526-229eec2b92dc&scope=oauth%20crm.objects.contacts.read%20crm.objects.contacts.write&redirect_uri=http://localhost:8080/oauth/callback"
    }
    ```
    
2. Após abrir a url recebida no passo 1, voce deverá consentir em instalar o Aplicativo MyHubSpotApp em sua conta de testes. Ao consentir você será redirecionado para uma paágina a api que exibirá seu access token.

![Página de Callback de autenticação da Api](https://drive.google.com/uc?export=view&id=10CAIhBTIyDoUft9YhRtatO_tWkc-tJSx)

3. Após isso você poderá enviar uma requisição para o endpoint contacts/create, enviando as informações para a criação de um contato.
    
    **Exemplo de Requisição**
    
    ```
    curl --request POST \
      --url http://localhost:8080/contacts/create \
      --header 'Authorization: Bearer [ACCES_TOKEN_GERADO_NA_CHAMADA_ANTERIOR]' \
      --header 'Content-Type: application/json' \
      --data '{
      "properties": {
        "email": "jonh.doe6@test.org",
        "lastname": "Jonh6",
        "firstname": "Doe2"
      }
    }'
    ```
    
    **Exemplo de Resposta**
    
    ```
    {
	"id": "106529700357",
	"properties": {
		"createdate": "2025-03-17T03:04:59.763Z",
		"email": "jonh.doe6@test.org",
		"firstname": "Doe2",
		"hs_all_contact_vids": "106529700357",
		"hs_associated_target_accounts": "0",
		"hs_currently_enrolled_in_prospecting_agent": "false",
		"hs_email_domain": "test.org",
		"hs_full_name_or_email": "Doe2 Jonh6",
		"hs_is_contact": "true",
		"hs_is_unworked": "true",
		"hs_lifecyclestage_lead_date": "2025-03-17T03:04:59.763Z",
		"hs_marketable_status": "false",
		"hs_marketable_until_renewal": "false",
		"hs_membership_has_accessed_private_content": "0",
		"hs_object_id": "106529700357",
		"hs_object_source": "INTEGRATION",
		"hs_object_source_id": "9318066",
		"hs_object_source_label": "INTEGRATION",
		"hs_pipeline": "contacts-lifecycle-pipeline",
		"hs_prospecting_agent_actively_enrolled_count": "0",
		"hs_registered_member": "0",
		"hs_sequences_actively_enrolled_count": "0",
		"lastmodifieddate": "2025-03-17T03:04:59.763Z",
		"lastname": "Jonh6",
		"lifecyclestage": "lead",
		"num_notes": "0"
	},
	"createdAt": "2025-03-17T03:04:59.763Z",
	"updatedAt": "2025-03-17T03:04:59.763Z",
	"archived": false
    }
    ```
    
4. Ao obter sucesso na criação de um contato, verifique o terminal em que a aplicação está rodando e verá que uma mensagem de confirmação de notificação do contato Criado no terminal em que a aplicação está rodando.
    Exemplo de Notificacao no Terminal
    
    ```
    ========== NOTIFICACAO DE CRIACAO DE CONTATOS =======
    Um contato foi criado em sua conta HubSpot com os seguintes dados
    [{"eventId":3944863254,"subscriptionId":3327050,"portalId":49532047,"appId":9318066,"occurredAt":1742175136625,"subscriptionType":"contact.creation","attemptNumber":7,"objectId":106500991877,"changeFlag":"CREATED","changeSource":"INTEGRATION","sourceId":"9318066"}]
    =====================================================
    ```
    
## ⚠️ Problemas enfrentados

Aqui estão listados alguns dos problemas enfrentados durante o desenvolvimento e como resolvê-los.

### Testar as notificações (WebHooks)

As configurações de Webhooks no HubSpot só permite o cadastro de urls de retorno HTTPS válidas, o que dificulta os testes em ambiente de desenvolvimento local. Para testar efetivamente os endpoins é necessário que se rode a aplicação em um ambiente com SSL configurado, o que adiciona complexidade ao desenvolvimento.

* **Como solucionar**: Para contornar este problema, utilizei o NGROK para expor a api rodando localmente através de um tunel HTTPS, o problema é que a url exposta é gerada dinamicamente pelo NGROK. Para contornar isso, foi implementado um endpoint que faz a atualização da URL de notificação no HubSpot. Para isso, faça o seguinte:

  * Instalar o NGROK seguindo este tutorial [Tutorial](https://thayto.com/blog/expondo-seu-localhost-com-ngrok)  
  * Configurar a **variavel de ambiente** NROK_BASE_URL com a URL HTTPS gerada após iniciar o serviço do NGROK.
  
![Tela do NGROK](https://drive.google.com/uc?export=view&id=16ZWaCk79sbj01RNI43A5dC4xMVanGJPf)

## 📄 Documentação Técnica

Aqui estão algumas informações técnicas acerca do projeto.

#### Decisões Técnicas de Desenvolvimento

**1. Framework:** O Spring Boot foi escolhido devido à sua robustez, facilidade de integração e suporte para desenvolvimento de APIs REST.

**2. WebFlux:** É uma biblioteca que fornece suporte para programação reativa, especialmente em cenários onde é necessário lidar com alto volume de solicitações de forma eficiente, com baixa latência e aproveitando recursos de hardware modernos. Apesar de ser uma integração simples e este potencial do webflux não estar sendo usado de fato aqui, a equipe do Spring recomenda o uso do cliente assincrino de REST presente no Webflux, o WebClient, pois o cliente Rest Template foi marcado como depreciado e será descontinuado em futuras versões do framework.

#### Motivação no uso das Libs

**1. WebClient:** Cliente HTTP reativo para integração com a API do HubSpot.

**2. Resilience4j:** Implementação de Rate Limiting para gerenciar limites de requisições da API do HubSpot. Ela Evita penalizações da API por ultrapassar limites de requisição.

**3. SLF4J/Logback:** Fornece logs detalhados para monitoramento e debugging, além de ser uma implementação nativa no Framework Spring.

**4. DevTools:** para obter as vantagens de se testar configurações, modificações etc. em runtime sem precisar ficar reazendo builds e rodando a aplicação a cada modificação.

#### Melhorias Futuras

**1. Persistência de Dados:** Adicionar uma camada de banco de dados para armazenar tokens OAuth e logs de notificações recebidas.

**2. Mensageria:** Integrar uma fila (ex.: RabbitMQ ou Kafka) para processar notificações Webhook de forma assíncrona.

**3. Documentação Automatizada:** Adicionar suporte ao Swagger/OpenAPI para disponibilizar uma documentação interativa dos endpoints.

**4. Testes Automatizados:** Implementar testes unitários e de integração para garantir a estabilidade da API.

**5. Docker:** Criar uma imagem Docker para facilitar a implantação em diferentes ambientes, além de facilitar a implantação em cloud.

