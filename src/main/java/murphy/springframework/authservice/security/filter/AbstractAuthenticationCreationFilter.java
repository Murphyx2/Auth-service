package murphy.springframework.authservice.security.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractAuthenticationCreationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) //
			throws ServletException, IOException {
		Authentication buildAuthentication = buildAuthentication(request);

		if(buildAuthentication != null) {
			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			securityContext.setAuthentication(buildAuthentication);
			SecurityContextHolder.setContext(securityContext);
		}

		filterChain.doFilter(request, response);

	}

	protected abstract Authentication buildAuthentication(HttpServletRequest request);
}
