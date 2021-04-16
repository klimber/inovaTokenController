package br.com.klimber.inova.model;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class CustomerProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(unique = true)
	private String name;

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<String> authorities;

	public CustomerProfile(@NotBlank String name, Set<String> authorities) {
		this.name = name;
		this.authorities = authorities;
	}

}
