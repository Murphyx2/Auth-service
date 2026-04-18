package murphy.springframework.authservice.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import murphy.springframework.authservice.security.authentication.JwtAuthentication;
import murphy.springframework.authservice.security.service.jwt.JwtService;
import murphy.springframework.authservice.security.user.AuthUser;
import org.jspecify.annotations.Nullable;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtService jwtService;

	public JwtAuthenticationProvider(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

		JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;

		AuthUser authUser = jwtService.resolveJwtToken(jwtAuthentication.jwtToken());

		return JwtAuthentication.authenticated(authUser);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthentication.class.isAssignableFrom(authentication);
	}
}
