package br.com.klimber.inova.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

	String oldPassword;
	String newPassword;

}
