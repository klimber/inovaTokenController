package br.com.klimber.inova.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import br.com.klimber.inova.service.PbiService;
import lombok.extern.java.Log;

@Component
@Log
@EnableScheduling
public class ScheduledTasks {

	@Autowired
	private PbiService pbiService;

//	@Scheduled(fixedRate = 3600000) // Every 1 hour
//	private void update() {
//		updateGroups();
//		updateReports();
//	}

	private void updateGroups() {
		log.info("Started update groups job");
		pbiService.updateGroups();
		log.info("Finished update groups job");
	}

	private void updateReports() {
		log.info("Started update reports job");
		pbiService.updateReports();
		log.info("Finished update reports job");
	}
}
