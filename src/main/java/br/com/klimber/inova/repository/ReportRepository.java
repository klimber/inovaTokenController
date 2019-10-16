package br.com.klimber.inova.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

	List<Report> findByGroupId(String groupId);

}
