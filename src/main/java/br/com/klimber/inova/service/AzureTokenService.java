package br.com.klimber.inova.service;

import java.time.Instant;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AzureTokenService {

	private static final String MS_POWERBI_SCOPE = "https://analysis.windows.net/powerbi/api/.default";
	private static final String MS_TOKEN_URL = "https://login.microsoftonline.com/{tenantId}/oauth2/v2.0/token";

	private final AzureTokenRepository azureTokenRepository;
	private final RestTemplate restTemplate;

	@Value("${app.azure.client-id}")
	private String azureClientId;
	@Value("${app.azure.client-secret}")
	private String azureClientSecret;
	@Value("${app.azure.tenant-id}")
	private String tenantId;

	private static AzureToken azureToken = null;

	public String getToken() {
		if (!isValidToken()) {
			renewToken();
		}
		return azureToken.getAccessToken();
	}

	public void renewToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(azureClientId, azureClientSecret);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "client_credentials");
		body.add("scope", MS_POWERBI_SCOPE);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		azureToken = restTemplate.postForObject(MS_TOKEN_URL, request, AzureToken.class, tenantId);
		azureTokenRepository.save(azureToken);
	}

	private boolean isValidToken() {
		if (azureToken == null) {
			if (azureTokenRepository.count() > 0) {
				azureToken = azureTokenRepository.findById(1L).get();
			} else {
				return false;
			}
		}
		return Instant.now().isBefore(azureToken.getExpiresOn());
	}

}
