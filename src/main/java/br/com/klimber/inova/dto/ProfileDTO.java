package br.com.klimber.inova.dto;

import jakarta.validation.constraints.NotBlank;

import br.com.klimber.inova.model.CustomerProfile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDTO {

	private Long id;

	@NotBlank
	private String name;

	public ProfileDTO(CustomerProfile profile) {
		this.id = profile.getId();
		this.name = profile.getName();
	}

}
