package br.com.klimber.inova.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.klimber.inova.model.EmbedToken;
import br.com.klimber.inova.model.Group;
import br.com.klimber.inova.model.PbiRestResponse;
import br.com.klimber.inova.model.Report;
import br.com.klimber.inova.service.AzureTokenService;

@RestController
public class PbiController {

	@Autowired
	private AzureTokenService azureTokenService;
	private RestTemplate restTemplate = new RestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	private HttpEntity<String> request;
	private static PbiRestResponse<Group> groups;
	private static PbiRestResponse<Report> reports;
	private EmbedToken embedToken;
	private ObjectMapper mapper = new ObjectMapper();

	@GetMapping("/pbi/groups")
	public PbiRestResponse<Group> getGroups() {
		String url = "https://api.powerbi.com/v1.0/myorg/groups";
		String azureToken = azureTokenService.getToken();
		headers.setBearerAuth(azureToken);
		request = new HttpEntity<>(headers);
		groups = restTemplate
				.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<PbiRestResponse<Group>>() {
				}).getBody();
		return groups;
	}

	@GetMapping("/pbi/reports")
	public PbiRestResponse<Report> getReportsInGroup(@RequestParam String groupId) {
		String url = "https://api.powerbi.com/v1.0/myorg/groups/" + groupId + "/reports";
		String azureToken = azureTokenService.getToken();
		headers.setBearerAuth(azureToken);
		request = new HttpEntity<>(headers);
		reports = restTemplate
				.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<PbiRestResponse<Report>>() {
				}).getBody();
		return reports;
	}

	@GetMapping("/pbi/embedToken")
	public EmbedToken postForEmbedToken(@RequestParam String groupId, @RequestParam String reportId)
			throws JsonProcessingException {
		String url = "https://api.powerbi.com/v1.0/myorg/groups/" + groupId + "/reports/" + reportId + "/GenerateToken";
		String azureToken = azureTokenService.getToken();
		headers.setBearerAuth(azureToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> body = new HashMap<>();
		body.put("accessLevel", "View");
		body.put("allowSaveAs", "false");
		request = new HttpEntity<String>(mapper.writeValueAsString(body), headers);
		embedToken = restTemplate.postForObject(url, request, EmbedToken.class);
		return embedToken;
	}

}
