package br.com.klimber.inova.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class Group {

	@JsonAlias("id")
	public String id;
	@JsonAlias("isReadOnly")
	public Boolean isReadOnly;
	@JsonAlias("isOnDedicatedCapacity")
	public Boolean isOnDedicatedCapacity;
	@JsonAlias("upstreamDataflowsOfDatasets")
	public List<Object> upstreamDataflowsOfDatasets = null;
	@JsonAlias("name")
	public String name;

}
