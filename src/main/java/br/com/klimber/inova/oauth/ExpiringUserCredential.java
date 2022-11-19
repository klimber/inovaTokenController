package br.com.klimber.inova.oauth;

import java.time.Instant;
import org.springframework.security.core.userdetails.UserDetails;

public record ExpiringUserCredential(UserDetails user, Instant expiresAt) {
}
