package br.com.klimber.inova.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${spring.profiles.active}")
	private String profile;
	private List<String> permitAll = new ArrayList<>();

	@Override
	public void configure(HttpSecurity http) throws Exception {
		permitAll.add("/todos/**");
		permitAll.add("/checkAllTodos");
		permitAll.add("/todosClearCompleted");
		permitAll.add("/initTodos");
		if (profile.equals("dev")) {
			permitAll.add("/h2-console/**");
		}

		http.cors().and().csrf().disable().authorizeRequests() //
				.antMatchers(permitAll.toArray(new String[0])).permitAll() //
				.antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll() //
				.anyRequest().authenticated(); //
	}
}
