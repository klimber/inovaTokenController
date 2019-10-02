package br.com.klimber.inova.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class EmbedToken {

	@JsonAlias("@odata.context")
	private String odataContext;
	private String token;
	private String tokenId;
	private Instant expiration;

}
