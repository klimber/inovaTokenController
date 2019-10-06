package br.com.klimber.inova.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.klimber.inova.model.AzureToken;
import br.com.klimber.inova.repository.AzureTokenRepository;

@Service
public class AzureTokenService {

	@Autowired
	private AzureTokenRepository azureTokenRepository;
	private static AzureToken azureToken = null;
	private final RestTemplate restTemplate = new RestTemplate();
	private final HttpHeaders headers = new HttpHeaders();
	private final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
	private final HttpEntity<MultiValueMap<String, String>> request;
	private final String tenantId;

	public AzureTokenService(@Value("${azure.app.client-id}") String azureClientId,
			@Value("${azure.app.client-secret}") String azureClientSecret,
			@Value("${azure.app.tenant-id}") String tenantId) {
		this.headers.setBasicAuth(azureClientId, azureClientSecret);
		this.headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		this.body.add("grant_type", "client_credentials");
		this.body.add("scope", "https://analysis.windows.net/powerbi/api/.default");
		this.request = new HttpEntity<MultiValueMap<String, String>>(body, headers);
		this.tenantId = tenantId;
	}

	public String getToken() {
		if (!isValidToken()) {
			renewToken();
		}
		return azureToken.getAccessToken();
	}

	public void renewToken() {
		azureToken = restTemplate.postForObject("https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token",
				request, AzureToken.class);
		azureTokenRepository.save(azureToken);
	}

	private boolean isValidToken() {
		if (azureToken == null) {
			if (azureTokenRepository.count() > 0) {
				azureToken = azureTokenRepository.getOne(1L);
			} else {
				return false;
			}
		}
		return Instant.now().isBefore(azureToken.getExpiresOn());
	}

}
