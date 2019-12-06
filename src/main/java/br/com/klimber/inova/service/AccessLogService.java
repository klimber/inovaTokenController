package br.com.klimber.inova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
