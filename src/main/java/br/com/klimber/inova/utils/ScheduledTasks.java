package br.com.klimber.inova.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.klimber.inova.service.AccessLogService;
import br.com.klimber.inova.service.PbiService;
import lombok.extern.java.Log;

@Component
@Log
@EnableScheduling
public class ScheduledTasks {

	@Autowired
	private PbiService pbiService;

	@Autowired
	private AccessLogService logService;
	@Value("${app.remove-log-after-days}")
	private int remove_log_days;

	@Transactional
	@Scheduled(fixedRate = 3600000) // Every 1 hour
	private void update() {
		log.info("Started 'update groups' job");
		pbiService.updateGroups();
		log.info("Finished 'update groups' job");
		log.info("Started 'update reports' job");
		pbiService.updateReports();
		log.info("Finished 'update reports' job");
	}

	@Scheduled(fixedRate = 86400000)
	private void deleteOldLogs() {
		log.info("Started 'delete old logs' job");
		logService.removeLogsOlderThan(remove_log_days);
		log.info("Finished 'delete old logs' job");
	}
}
