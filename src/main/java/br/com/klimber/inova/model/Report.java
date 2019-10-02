package br.com.klimber.inova.model;

import lombok.Data;

@Data
public class Report {

	private String id;
	private String reportType;
	private String name;
	private String webUrl;
	private String embedUrl;
	private boolean isFromPbix;
	private boolean isOwnedByMe;
	private String datasetId;

}
