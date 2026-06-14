package murphy.springframework.authservice.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import murphy.springframework.authservice.security.user.AuthUser;

@Component
public class SecurityUtils {

	public static AuthUser getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !(auth.getPrincipal() instanceof AuthUser user)) {
			throw new SecurityException("User not authenticated");
		}
		return user;
	}
}
