package br.com.klimber.inova.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Entity
@Data
@Table(name = "WORKSPACES")
public class Group {

	@Id
	@JsonAlias("id")
	private String groupId;
	private Boolean isReadOnly;
	private Boolean isOnDedicatedCapacity;
//	@JsonAlias("upstreamDataflowsOfDatasets")
//	private List<Object> upstreamDataflowsOfDatasets = null;
	private String name;
}
