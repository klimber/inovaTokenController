package br.com.klimber.inova.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;

@Configuration
@EnableWebSecurity
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

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (customerRepository.count() == 0) {
			Customer admin = Customer.builder() //
					.email(adminEmail) //
					.username(adminUsername) //
					.password(passwordEncoder().encode(adminPassword)) //
					.role("ROLE_ADMIN") //
					.firstName(adminFirstName) //
					.lastName(adminLastName).build(); //
			customerRepository.saveAndFlush(admin);
		}
		auth.userDetailsService(username -> {
			return customerRepository.findByUsernameIgnoreCase(username);
		}).passwordEncoder(passwordEncoder());
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		config.addAllowedMethod(HttpMethod.DELETE);
		config.addAllowedMethod(HttpMethod.PATCH);
		source.registerCorsConfiguration("/todoApp/**", config);
		config.setAllowedOrigins(List.of("http://localhost:8081"));
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/oauth/token", config);

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(-1);
		return bean;
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
}
