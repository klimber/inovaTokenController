package br.com.klimber.inova.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer implements UserDetails, OAuth2AuthenticatedPrincipal {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String username;
	@NotBlank
	private String fullName;

	private String extraInfo;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private CustomerProfile profile;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> extraAuthorities;

	@Override
	public Map<String, Object> getAttributes() {
		return Map.of();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Stream<String> profile = this.profile.getAuthorities().stream();
		Stream<String> extra = this.getExtraAuthorities().stream();
		return Stream.concat(profile, extra).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return this.getUsername();
	}
}
