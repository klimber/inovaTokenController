package br.com.klimber.inova.endpoint;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.klimber.inova.model.EmbedToken;
import br.com.klimber.inova.model.Group;
import br.com.klimber.inova.model.Report;
import br.com.klimber.inova.service.PbiService;

@RestController
public class PbiController {

	@Autowired
	private PbiService pbiService;

	@GetMapping("/pbi/groups")
	public List<Group> getGroups() {
		return pbiService.getGroups();
	}

	@GetMapping("/pbi/groups/{groupId}/reports")
	public List<Report> getReportsInGroup(@PathVariable("groupId") String groupId) {
		Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(a -> a.getAuthority()).collect(Collectors.toSet());
		List<Report> reports = pbiService.getReportsInGroup(groupId);
		if (authorities.contains("ROLE_ADMIN")) {
			return reports;
		}
		return reports.stream().filter(report -> authorities.contains(report.getReportId()))
				.collect(Collectors.toList());
	}

	@GetMapping("/pbi/groups/{groupId}/reports/{reportId}/GenerateToken")
	public EmbedToken postForEmbedToken(@PathVariable("groupId") String groupId,
			@PathVariable("reportId") String reportId) throws JsonProcessingException {
		Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(a -> a.getAuthority()).collect(Collectors.toSet());
		if (authorities.contains("ROLE_ADMIN") || authorities.contains(reportId)) {
			return pbiService.getReportEmbedToken(groupId, reportId);
		}
		throw new UnauthorizedUserException("User does not have permission for this resource");
	}

}
