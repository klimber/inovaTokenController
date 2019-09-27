package br.com.klimber.inova.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.klimber.inova.model.AzureToken;

@Service
public class AzureTokenService {

	private static AzureToken azureToken = new AzureToken();
	private final RestTemplate restTemplate = new RestTemplate();
	private final HttpHeaders headers;
	private final MultiValueMap<String, String> body;
	private final HttpEntity<MultiValueMap<String, String>> request;
	private final String tenantId;

	public AzureTokenService(@Value("${azure.app.client-id}") String azureClientId,
			@Value("${azure.app.client-secret}") String azureClientSecret,
			@Value("${azure.app.tenant-id}") String tenantId) {
		headers = initializeHeaders(azureClientId, azureClientSecret);
		body = initializeBody();
		request = new HttpEntity<MultiValueMap<String, String>>(body, headers);
		this.tenantId = tenantId;
	}

	private MultiValueMap<String, String> initializeBody() {
		MultiValueMap<String, String> bodyInit = new LinkedMultiValueMap<>();
		bodyInit.add("grant_type", "client_credentials");
		bodyInit.add("resource", "https://analysis.windows.net/powerbi/api");
		return bodyInit;
	}

	private HttpHeaders initializeHeaders(String azureClientId, String azureClientSecret) {
		HttpHeaders headersInit = new HttpHeaders();
		headersInit.setBasicAuth(azureClientId, azureClientSecret);
		headersInit.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headersInit;
	}

	public String getToken() {
		if (!isValidToken()) {
			renewToken();
		}
		return azureToken.getAccess_token();
	}

	public void renewToken() {
		azureToken = restTemplate.postForObject("https://login.microsoftonline.com/" + tenantId + "/oauth2/token",
				request, AzureToken.class);
	}

	private boolean isValidToken() {
		if (azureToken.getExpires_on() == null)
			return false;
		return ZonedDateTime.now(ZoneId.of("UTC")).toInstant().compareTo(azureToken.getExpires_on()) > 0;
	}

}
