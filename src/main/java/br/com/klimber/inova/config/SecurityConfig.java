package br.com.klimber.inova.config;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.model.CustomerProfile;
import br.com.klimber.inova.repository.CustomerProfileRepository;
import br.com.klimber.inova.service.CustomerService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
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

	private final CustomerProfileRepository profileRepository;
	private final CustomerService customerService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (customerService.count() == 0) {
			CustomerProfile initProfile = new CustomerProfile("Admin Profile", Set.of("ROLE_ADMIN"));
			initProfile = profileRepository.save(initProfile);
			Customer admin = Customer.builder() //
					.email(adminEmail) //
					.username(adminUsername) //
					.password(passwordEncoder().encode(adminPassword)) //
					.profile(initProfile)
					.fullName(adminFullName) //
					.extraInfo(adminExtraInfo).build(); //
			customerService.save(admin);
		}
		auth.userDetailsService(customerService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> filterRegistrationBean() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		setAuthEndpointConfig(source);
		setGeneralEndpointConfig(source);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	private void setGeneralEndpointConfig(UrlBasedCorsConfigurationSource source) {
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedMethod("*");
		config.setAllowedOrigins(List.of(url));
		if (profile.equals("dev")) {
			config.addAllowedOrigin("http://localhost:8080");
			config.addAllowedOrigin("http://localhost:8081");
		}
		source.registerCorsConfiguration("/**", config);
	}

	private void setAuthEndpointConfig(UrlBasedCorsConfigurationSource source) {
		CorsConfiguration authConfig = new CorsConfiguration().applyPermitDefaultValues();
		authConfig.addAllowedMethod("*");
		authConfig.setAllowCredentials(true);
		authConfig.setAllowedOrigins(List.of(url));
		if (profile.equals("dev")) {
			authConfig.addAllowedOrigin("http://localhost:8080");
			authConfig.addAllowedOrigin("http://localhost:8081");
		}
		source.registerCorsConfiguration("/oauth/**", authConfig);
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
