package br.com.klimber.inova.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
@Entity
public class EmbedToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String reportId;
	private String groupId;
	@JsonAlias("@odata.context")
	private String odataContext;
	@Column(length = 2000)
	private String token;
	private String tokenId;
	private Instant expiration;

	public boolean isValid() {
		return (expiration != null && Instant.now().isBefore(expiration));
	}

}
