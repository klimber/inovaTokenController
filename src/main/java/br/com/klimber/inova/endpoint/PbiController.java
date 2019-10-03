package br.com.klimber.inova.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
		return pbiService.getReportsInGroup(groupId);
	}

	@GetMapping("/pbi/groups/{groupId}/reports/{reportId}/GenerateToken")
	public EmbedToken postForEmbedToken(@RequestParam String groupId, @RequestParam String reportId)
			throws JsonProcessingException {
		return pbiService.getReportEmbedToken(groupId, reportId);
	}

}
