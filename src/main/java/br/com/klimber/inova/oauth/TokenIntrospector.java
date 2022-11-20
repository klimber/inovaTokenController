package br.com.klimber.inova.oauth;

import br.com.klimber.inova.model.Customer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenIntrospector implements OpaqueTokenIntrospector {
    private final TokenRepository tokenRepository;

    /**
     * This method is called every request to check if the user token is valid.
     *
     * <p>Verify if the provided token is in the repository and return an authenticated principal.</p>
     */
    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        Optional<UserDetails> userDetails = tokenRepository.get(token);
        return userDetails.map(Customer.class::cast).orElseThrow(() -> new BadOpaqueTokenException("Invalid Token"));
    }
}
