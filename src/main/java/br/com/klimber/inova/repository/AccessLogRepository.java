package br.com.klimber.inova.repository;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.AccessLog;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

	@Modifying
	@Query("delete from AccessLog where date < ?1")
	public void deleteOlderThanDate(Timestamp date);

}
