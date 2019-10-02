package br.com.klimber.inova.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class PbiRestResponse<T> {

	@JsonAlias("@odata.context")
	private String odataContext;
	@JsonAlias("@odata.count")
	private Integer odataCount;
	@JsonAlias("value")
	private List<T> value;
}
