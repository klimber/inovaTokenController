package br.com.klimber.inova.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.klimber.inova.model.CustomerProfile;
import br.com.klimber.inova.repository.CustomerProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProfileService {

	private final CustomerProfileRepository profileRepository;

	public CustomerProfile findById(Long id) {
		return profileRepository.findById(id).get();
	}

	@Transactional
	public CustomerProfile findByIdWithAuthorities(Long id) {
		CustomerProfile profile = profileRepository.findById(id).get();
		// load authorities in transaction
		profile.getAuthorities().size();
		return profile;

	}

	public CustomerProfile save(CustomerProfile profile) {
		return profileRepository.save(profile);
	}

	public List<CustomerProfile> findAll() {
		return profileRepository.findAll();
	}

	public void deleteById(Long id) {
		profileRepository.deleteById(id);
	}

	@Transactional
	public void addAuthority(Long profileId, String authority) {
		CustomerProfile profile = profileRepository.findById(profileId).get();
		profile.getAuthorities().add(authority);
		profileRepository.save(profile);
	}

	@Transactional
	public void removeAuthority(Long profileId, String authority) {
		CustomerProfile profile = profileRepository.findById(profileId).get();
		profile.getAuthorities().remove(authority);
		profileRepository.save(profile);
	}

}
