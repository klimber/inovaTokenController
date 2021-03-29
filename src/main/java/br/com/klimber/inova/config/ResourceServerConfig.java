package br.com.klimber.inova.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableAuthorizationServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${spring.profiles.active}")
	private String profile;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		List<String> permitAll = new ArrayList<>();
		permitAll.add("/todoApp/initTodos");
		if (profile.equals("dev")) {
			permitAll.add("/h2-console/**");
		}

		http.authorizeRequests() //
				.antMatchers(HttpMethod.OPTIONS).permitAll() //
				.antMatchers(permitAll.toArray(new String[0])).permitAll() //
				.anyRequest().authenticated(); //
	}
}
