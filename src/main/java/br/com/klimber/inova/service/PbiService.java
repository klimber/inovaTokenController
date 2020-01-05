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
import br.com.klimber.inova.repository.GroupRepository;
import br.com.klimber.inova.repository.ReportRepository;

@Service
public class PbiService {

	@Autowired
	private AzureTokenService azureTokenService;
	@Autowired
	private EmbedTokenRepository embedTokenRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private ReportRepository reportRepository;
	private static RestTemplate restTemplate = new RestTemplate();
	private static ObjectMapper mapper = new ObjectMapper();

	public List<Group> getGroups() {
		return groupRepository.findAll();
	}

	public List<Report> getReportsInGroup(String groupId) {
		return reportRepository.findByGroupId(groupId);
	}

	public EmbedToken getReportEmbedToken(@RequestParam String groupId, @RequestParam String reportId)
			throws JsonProcessingException {
		EmbedToken embedToken = embedTokenRepository.findByGroupIdAndReportId(groupId, reportId).orElse(null);
		Long currentTokenId = null;
		if (embedToken != null) {
			currentTokenId = embedToken.getId();
		}
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
			embedToken.setId(currentTokenId);
			embedToken.setReportId(reportId);
			embedToken.setGroupId(groupId);
			embedTokenRepository.save(embedToken);
		}
		return embedToken;
	}

	public void updateGroups() {
		String url = "https://api.powerbi.com/v1.0/myorg/groups";
		String azureToken = azureTokenService.getToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(azureToken);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		List<Group> groups = restTemplate
				.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<PbiRestResponse<Group>>() {
				}).getBody().getValue();
		groupRepository.deleteAll();
		groupRepository.saveAll(groups);
	}

	public void updateReports() {
		List<Group> groups = groupRepository.findAll();
		for (Group group : groups) {
			String url = "https://api.powerbi.com/v1.0/myorg/groups/" + group.getGroupId() + "/reports";
			String azureToken = azureTokenService.getToken();
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(azureToken);
			HttpEntity<Object> request = new HttpEntity<>(headers);
			List<Report> reports = restTemplate
					.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<PbiRestResponse<Report>>() {
					}).getBody().getValue();
			reports.forEach(report -> report.setGroupId(group.getGroupId()));
			reportRepository.deleteAll();
			reportRepository.saveAll(reports);
		}
	}
}
