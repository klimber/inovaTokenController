package br.com.klimber.inova.config;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${customer.admin.username}")
	private String adminUsername;
	@Value("${customer.admin.password}")
	private String adminPassword;
	@Value("${customer.admin.email}")
	private String adminEmail;
	@Value("${customer.admin.fullname}")
	private String adminFullName;
	@Value("${customer.admin.extraInfo}")
	private String adminExtraInfo;
	@Value("${spring.profiles.active}")
	private String profile;
	@Value("${app.url}")
	private String url;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (customerRepository.count() == 0) {
			Customer admin = Customer.builder() //
					.email(adminEmail) //
					.username(adminUsername) //
					.password(passwordEncoder().encode(adminPassword)) //
					.authorities(Set.of("ROLE_ADMIN")) //
					.fullName(adminFullName) //
					.extraInfo(adminExtraInfo).build(); //
			customerRepository.saveAndFlush(admin);
		}
		auth.userDetailsService(username -> {
			return customerRepository.findByUsernameIgnoreCase(username)
					.orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
		}).passwordEncoder(passwordEncoder());
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> filterRegistrationBean() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedMethod("*");
		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of(url));
		if (profile.equals("dev")) {
			config.addAllowedOrigin("http://localhost:8080");
			config.addAllowedOrigin("http://localhost:8081");
		}
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
