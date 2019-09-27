package br.com.klimber.inova.model;

import java.time.Instant;

import lombok.Data;

@Data
public class AzureToken {

	private String token_type;
	private Long expires_in; // seconds
	private Long ext_expires_in; // seconds
	private Instant expires_on; // UTC
	private Instant not_before; // UTC
	private String resource;
	private String access_token;

	public void setExpires_on(Long expires_on) {
		this.expires_on = Instant.ofEpochSecond(expires_on);
	}

	public void setNot_before(Long not_before) {
		this.not_before = Instant.ofEpochSecond(not_before);
	}
}
