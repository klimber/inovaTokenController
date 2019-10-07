package br.com.klimber.inova.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.EmbedToken;

@Repository
public interface EmbedTokenRepository extends JpaRepository<EmbedToken, Long> {

	public Optional<EmbedToken> findByGroupIdAndReportId(String groupId, String reportId);

}
