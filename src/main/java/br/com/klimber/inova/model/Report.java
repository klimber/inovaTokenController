package br.com.klimber.inova.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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
	@Column(length = 1024)
	private String embedUrl;
	private boolean isFromPbix;
	private boolean isOwnedByMe;
	private String datasetId;

}
