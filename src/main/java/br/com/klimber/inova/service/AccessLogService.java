package br.com.klimber.inova.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.klimber.inova.model.AccessLog;
import br.com.klimber.inova.repository.AccessLogRepository;

@Service
public class AccessLogService {

	@Autowired
	private AccessLogRepository accessLogRepository;

	public void addLogEntry(String groupId, String reportId, Long customerId) {
		AccessLog accessLog = new AccessLog(customerId, groupId, reportId);
		accessLogRepository.save(accessLog);
	}

	@Transactional
	public void removeLogsOlderThan(int days) {
		Timestamp removeBefore = Timestamp.from(Instant.now().minus(days, ChronoUnit.DAYS));
		accessLogRepository.deleteOlderThanDate(removeBefore);
	}

}
