package br.com.klimber.inova.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchCustomerDTOAdmin {

	private String username;

	private String email;

	private String fullName;

	private String extraInfo;

	private String password;

	private Boolean isAdmin;

}
