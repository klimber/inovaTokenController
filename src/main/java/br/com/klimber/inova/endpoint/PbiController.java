package br.com.klimber.inova.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import br.com.klimber.inova.service.AzureTokenService;

@RestController
public class PbiController {

	@Autowired
	private AzureTokenService azureTokenService;
	private RestTemplate restTemplate = new RestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	private MultiValueMap<String, String> body;
	private HttpEntity<String> request;
	private String tenantId;

	@GetMapping("/pbi/groups")
	public ResponseEntity<String> getGroups() {
		String url = "https://api.powerbi.com/v1.0/myorg/groups";
		String azureToken = azureTokenService.getToken();
		headers.setBearerAuth(azureToken);
		headers.setAccept(List.of(MediaType.ALL));
		request = new HttpEntity<>(headers);
		return restTemplate.getForEntity(url, String.class, request);
	}

}
