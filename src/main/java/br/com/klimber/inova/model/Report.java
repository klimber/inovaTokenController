package br.com.klimber.inova.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Entity
@Data
public class Report {

	@Id
	@JsonAlias("id")
	private String reportId;
	private String groupId;
	private String reportType;
	private String name;
	private String webUrl;
	private String embedUrl;
	private boolean isFromPbix;
	private boolean isOwnedByMe;
	private String datasetId;

}
