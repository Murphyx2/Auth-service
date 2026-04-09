package murphy.springframework.authservice.security.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murphy.springframework.authservice.common.AuthConstants;
import murphy.springframework.authservice.security.authentication.UserAuthentication;
import murphy.springframework.authservice.security.exception.TokenAuthenticationException;
import murphy.springframework.authservice.security.service.jwt.JwtService;
import murphy.springframework.authservice.security.user.AuthUser;

@Component
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	public SecurityAuthenticationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authenticationHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

		if(authenticationHeader == null) {
			// Authentication token is not present, let's rely on anonymous authentication
			filterChain.doFilter(request, response);
			return;
		}

		String token = stripBearerPrefix(authenticationHeader);
		AuthUser authUser = jwtService.resolveJwtToken(token);

		UserAuthentication userAuthentication = new UserAuthentication(authUser);

		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(userAuthentication);
		SecurityContextHolder.setContext(securityContext);

		filterChain.doFilter(request, response);
	}

	String stripBearerPrefix(String token) {

		if (!token.startsWith("Bearer")) {
			throw new TokenAuthenticationException("Unsupported authentication scheme");
		}

		return token.substring(7);
	}
}
