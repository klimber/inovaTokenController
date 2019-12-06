package br.com.klimber.inova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.AccessLog;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

}
