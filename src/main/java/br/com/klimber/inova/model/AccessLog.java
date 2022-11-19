package br.com.klimber.inova.model;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class AccessLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private Long customerId;
	@NotBlank
	private String groupId;
	@NotBlank
	private String reportId;
	@NotNull
	private final Timestamp date;

	public AccessLog(Long customerId, String groupId, String reportId) {
		super();
		this.customerId = customerId;
		this.groupId = groupId;
		this.reportId = reportId;
		this.date = Timestamp.from(Instant.now());
	}

}
