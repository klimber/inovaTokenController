package br.com.klimber.inova.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class AzureToken {

	@JsonAlias("token_type")
	private String tokenType;
	@JsonAlias("expires_in")
	private Long expiresIn; // seconds
	@JsonAlias("ext_expires_in")
	private Long extExpiresIn; // seconds
	private Instant expiresOn;
	@JsonAlias("access_token")
	private String accessToken;

	public void setExpires_in(Long expires_in) {
		this.expiresIn = expires_in;
		this.expiresOn = Instant.now().plus(expires_in, ChronoUnit.SECONDS);
	}
}
