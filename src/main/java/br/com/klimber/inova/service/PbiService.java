package br.com.klimber.inova.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.klimber.inova.model.EmbedToken;
import br.com.klimber.inova.model.Group;
import br.com.klimber.inova.model.PbiRestResponse;
import br.com.klimber.inova.model.Report;
import br.com.klimber.inova.repository.EmbedTokenRepository;

@Service
public class PbiService {

	@Autowired
	private AzureTokenService azureTokenService;
	@Autowired
	private EmbedTokenRepository embedTokenRepository;
	private static RestTemplate restTemplate = new RestTemplate();
	private static ObjectMapper mapper = new ObjectMapper();

	public List<Group> getGroups() {
		String url = "https://api.powerbi.com/v1.0/myorg/groups";
		String azureToken = azureTokenService.getToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(azureToken);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		List<Group> groups = restTemplate
				.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<PbiRestResponse<Group>>() {
				}).getBody().getValue();
		return groups;
	}

	public List<Report> getReportsInGroup(String groupId) {
		String url = "https://api.powerbi.com/v1.0/myorg/groups/" + groupId + "/reports";
		String azureToken = azureTokenService.getToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(azureToken);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		List<Report> reports = restTemplate
				.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<PbiRestResponse<Report>>() {
				}).getBody().getValue();
		return reports;
	}

	public EmbedToken getReportEmbedToken(@RequestParam String groupId, @RequestParam String reportId)
			throws JsonProcessingException {
		EmbedToken embedToken = embedTokenRepository.findByGroupIdAndReportId(groupId, reportId).orElse(null);
		if (embedToken == null || !embedToken.isValid()) {
			String url = "https://api.powerbi.com/v1.0/myorg/groups/" + groupId + "/reports/" + reportId
					+ "/GenerateToken";
			String azureToken = azureTokenService.getToken();
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(azureToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			Map<String, String> body = new HashMap<>();
			body.put("accessLevel", "View");
			body.put("allowSaveAs", "false");
			HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(body), headers);
			embedToken = restTemplate.postForObject(url, request, EmbedToken.class);
			embedToken.setReportId(reportId);
			embedToken.setGroupId(groupId);
			embedTokenRepository.save(embedToken);
		}
		return embedToken;
	}

}
