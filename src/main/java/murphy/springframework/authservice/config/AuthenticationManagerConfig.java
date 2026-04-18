package murphy.springframework.authservice.config;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class AuthenticationManagerConfig {

	@Bean
	public AuthenticationManager authenticationManager(
			List<AuthenticationProvider> authenticationProviders,
			ApplicationEventPublisher applicationEventPublisher
	) {

		ProviderManager providerManager = new ProviderManager(authenticationProviders);

		providerManager.setAuthenticationEventPublisher(
				new DefaultAuthenticationEventPublisher(applicationEventPublisher)
		);

		return providerManager;
	}
}
