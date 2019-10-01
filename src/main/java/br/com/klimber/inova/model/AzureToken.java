package br.com.klimber.inova.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.Data;

@Data
public class AzureToken {

	private String token_type;
	private Long expires_in; // seconds
	private Long ext_expires_in; // seconds
	private Instant expiresOn;
	private String access_token;

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
		this.expiresOn = Instant.now().plus(expires_in, ChronoUnit.SECONDS);
	}
}
