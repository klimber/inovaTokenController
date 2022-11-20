package br.com.klimber.inova.config;

import br.com.klimber.inova.service.CustomerService;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    /**
     * This method is how we set most of the web security configirations.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
        Set up Oauth2 resource server using an opaque token, meaning that an arbitrary token is passed on every request.
        An OpaqueTokenIntrospector bean is required for matching the tokens to actual users, provided by the
        TokenIntrospector class.
         */
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
        // Disable sessions as we are using Bearer token authentication (passed with every request)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Disable CSRF as this application won't have a session
        http.csrf(AbstractHttpConfigurer::disable);
        // Make sure basic authentication is disabled
        http.httpBasic(AbstractHttpConfigurer::disable);
        // This is a REST application, remove the login form
        http.formLogin(AbstractHttpConfigurer::disable);
        // As well as the logout form
        http.logout(AbstractHttpConfigurer::disable);
        // Enable CORS, defaults are pretty permissive, should work with most services
        http.cors(Customizer.withDefaults());
        // Allow POST requests on the token API for obtaining tokens
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/oauth/token").permitAll()
                                               // Everything else authenticated
                                               .anyRequest().authenticated());
        return http.build();
    }

    /**
     * DaoAuthenticationProvider provides an opinionated way of authenticating users via username and password.
     *
     * <p>It's being provided as a bean and consumed by {@link br.com.klimber.inova.oauth.TokenResource} to
     * authenticate users and provide them with tokens for later use in token authentication.</p>
     *
     * @param userDetailsService An implementation of {@link UserDetailsService}, such as {@link CustomerService}
     * @return The authentication provider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               PasswordEncoder encoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(encoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecureRandom secureRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
