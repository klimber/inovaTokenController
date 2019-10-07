package br.com.klimber.inova.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
