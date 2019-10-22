package br.com.klimber.inova.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;

@Configuration
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${customer.admin.username}")
	private String adminUsername;
	@Value("${customer.admin.password}")
	private String adminPassword;
	@Value("${customer.admin.email}")
	private String adminEmail;
	@Value("${customer.admin.firstname}")
	private String adminFirstName;
	@Value("${customer.admin.lastname}")
	private String adminLastName;
	@Value("${spring.profiles.active}")
	private String profile;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (customerRepository.count() == 0) {
			Customer admin = Customer.builder().email(adminEmail).username(adminUsername)
					.password(passwordEncoder().encode(adminPassword)).role("ROLE_ADMIN").firstName(adminFirstName)
					.lastName(adminLastName).build();
			customerRepository.saveAndFlush(admin);
		}
		auth.userDetailsService(username -> {
			return customerRepository.findByUsernameIgnoreCase(username).get();
		}).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
//		Ignore h2-console security on development so we can access it
		if (profile.equals("dev")) {
			web.ignoring().antMatchers("/h2-console/**", "/**");
		}
	}

}
