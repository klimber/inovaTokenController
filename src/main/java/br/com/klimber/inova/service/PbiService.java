package br.com.klimber.inova.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.klimber.inova.model.EmbedToken;
import br.com.klimber.inova.model.Group;
import br.com.klimber.inova.model.PbiRestResponse;
import br.com.klimber.inova.model.Report;
import br.com.klimber.inova.repository.EmbedTokenRepository;
import br.com.klimber.inova.repository.GroupRepository;
import br.com.klimber.inova.repository.ReportRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PbiService {

	private static final String MS_WORKSPACES_URL = "https://api.powerbi.com/v1.0/myorg/groups";
	private static final String MS_REPORTS_URL = "https://api.powerbi.com/v1.0/myorg/groups/{groupId}/reports";
	private static final String MS_EMBEDTOKEN_URL = "https://api.powerbi.com/v1.0/myorg/groups/{groupId}/reports/{reportId}/GenerateToken";

	private final AzureTokenService azureTokenService;
	private final EmbedTokenRepository embedTokenRepository;
	private final GroupRepository groupRepository;
	private final ReportRepository reportRepository;
	private final RestTemplate restTemplate;

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
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(azureTokenService.getToken());
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, String> body = new HashMap<>();
			body.put("accessLevel", "View");
			body.put("allowSaveAs", "false");

			HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

			embedToken = restTemplate.postForObject(MS_EMBEDTOKEN_URL, request, EmbedToken.class, groupId, reportId);
			embedToken.setId(currentTokenId);
			embedToken.setReportId(reportId);
			embedToken.setGroupId(groupId);
			embedTokenRepository.save(embedToken);
		}
		return embedToken;
	}

	@RegisterReflectionForBinding(PbiRestResponse.class)
	public void updateGroups() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(azureTokenService.getToken());

		HttpEntity<Object> request = new HttpEntity<>(headers);

		List<Group> groups = restTemplate
				.exchange(MS_WORKSPACES_URL, HttpMethod.GET, request,
						new ParameterizedTypeReference<PbiRestResponse<Group>>() {})
				.getBody()
				.getValue();

		reportRepository.deleteAll();
		groupRepository.deleteAll();
		groupRepository.saveAll(groups);
	}

	@RegisterReflectionForBinding(PbiRestResponse.class)
	public void updateReports() {
		List<Group> groups = groupRepository.findAll();
		for (Group group : groups) {
			String groupId = group.getGroupId();

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(azureTokenService.getToken());

			HttpEntity<Object> request = new HttpEntity<>(headers);

			List<Report> reports = restTemplate
					.exchange(MS_REPORTS_URL, HttpMethod.GET, request,
							new ParameterizedTypeReference<PbiRestResponse<Report>>() {}, groupId)
					.getBody()
					.getValue();
			reports.forEach(report -> report.setGroupId(groupId));
			reportRepository.saveAll(reports);
		}
	}
}
