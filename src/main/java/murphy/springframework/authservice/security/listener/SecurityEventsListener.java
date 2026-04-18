package murphy.springframework.authservice.security.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SecurityEventsListener {

	@EventListener(AuthenticationSuccessEvent.class)
	public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
		log.info("Authentication SUCCESS event received. Authentication: {}", event.getAuthentication());
	}

	// Beware, this could contain the credentials, so, It should be avoided to log this on production
	@EventListener(AbstractAuthenticationFailureEvent.class)
	public void handleAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
		log.warn(
				"Authentication FAILURE event received. Authentication: {}", event.getAuthentication());
	}
}
