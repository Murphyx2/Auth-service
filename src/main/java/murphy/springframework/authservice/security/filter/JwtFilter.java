package murphy.springframework.authservice.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import murphy.springframework.authservice.common.AuthConstants;
import murphy.springframework.authservice.security.authentication.JwtAuthentication;
import murphy.springframework.authservice.security.exception.TokenAuthenticationException;

@Component
public class JwtFilter extends AbstractAuthenticationCreationFilter{


	@Override
	protected Authentication buildAuthentication(HttpServletRequest request) {

		String authenticationHeader = request.getHeader(AuthConstants.JWT_AUTHORIZATION_HEADER);

		if( authenticationHeader == null || authenticationHeader.isEmpty() ) {
			return null;
		}

		String jwtToken = stripBearerPrefix(authenticationHeader);

		return JwtAuthentication.unauthenticated(jwtToken);
	}

	String stripBearerPrefix(String token) {
		if(!token.startsWith("Bearer ")) {
			throw new TokenAuthenticationException("Unsupported authentication scheme");
		}

		return token.substring(7);
	}
}
