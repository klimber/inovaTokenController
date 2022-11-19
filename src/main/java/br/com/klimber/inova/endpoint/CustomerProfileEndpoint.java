package br.com.klimber.inova.endpoint;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import br.com.klimber.inova.dto.ProfileDTO;
import br.com.klimber.inova.model.CustomerProfile;
import br.com.klimber.inova.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class CustomerProfileEndpoint {

	private final CustomerProfileService profileService;

	@Secured("ROLE_ADMIN")
	@PostMapping("/profiles")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CustomerProfile addProfile(@RequestBody @Valid ProfileDTO profileDTO) {
		CustomerProfile profile = new CustomerProfile(profileDTO.getName(), Set.of("ROLE_USER"));
		return profileService.save(profile);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/profiles/{id}")
	public void delProfile(@PathVariable Long id) {
		profileService.deleteById(id);
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping("/profiles/{id}")
	public ProfileDTO patchProfile(@PathVariable Long id, @RequestBody @Valid ProfileDTO profileDTO) {
		CustomerProfile profile = profileService.findById(id);
		profile.setName(profileDTO.getName());
		return new ProfileDTO(profileService.save(profile));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/profiles/{id}")
	public CustomerProfile getProfile(@PathVariable Long id) {
		return profileService.findByIdWithAuthorities(id);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/profiles")
	public List<ProfileDTO> getProfiles() {
		return profileService.findAll().stream().map(ProfileDTO::new)
				.collect(Collectors.toList());
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/profiles/{profileId}/authority/{authority}")
	public void addAuthority(@PathVariable Long profileId, @PathVariable String authority) {
		profileService.addAuthority(profileId, authority);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/profiles/{profileId}/authority/{authority}")
	public void removeAuthority(@PathVariable Long profileId, @PathVariable String authority) {
		profileService.removeAuthority(profileId, authority);
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/profiles/{profileId}")
	public CustomerProfile putProfile(@PathVariable Long profileId, @RequestBody @Valid CustomerProfile profile) {
		// ensure exists
		profileService.findById(profileId);
		profile.setId(profileId);
		return profileService.save(profile);
	}

}
