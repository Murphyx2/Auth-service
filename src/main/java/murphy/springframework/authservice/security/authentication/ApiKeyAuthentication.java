package murphy.springframework.authservice.security.authentication;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import murphy.springframework.authservice.security.user.AuthApp;
import org.jspecify.annotations.Nullable;

public record ApiKeyAuthentication(AuthApp authApp, boolean authenticated, String apiKey) implements Authentication {

	public static ApiKeyAuthentication unauthenticated(String apiKey) {
		return new ApiKeyAuthentication(null, false, apiKey);
	}

	public static ApiKeyAuthentication authenticated(AuthApp authApp) {
		return new ApiKeyAuthentication(authApp, true, null);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authApp.scopes().stream() //
				.map(Enum::name) //
				.map(SimpleGrantedAuthority::new) //
				.collect(Collectors.toSet());
	}

	@Override
	public @Nullable Object getCredentials() {
		return apiKey;
	}

	@Override
	public @Nullable Object getDetails() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public @Nullable Object getPrincipal() {
		return authApp;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}
}
