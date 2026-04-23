package murphy.springframework.authservice.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import murphy.springframework.authservice.config.properties.ApiKeyClientsProperties;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.security.authentication.ApiKeyAuthentication;
import murphy.springframework.authservice.security.service.apikey.ApikeyService;
import org.jspecify.annotations.Nullable;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

	private final ApikeyService apikeyService;

	public ApiKeyAuthenticationProvider(ApikeyService apikeyService) {
		this.apikeyService = apikeyService; //
	}

	@Override
	public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

		ApiKeyAuthentication apiKeyAuthentication = (ApiKeyAuthentication) authentication;

		String apiKey = apiKeyAuthentication.apiKey();

		return ApiKeyAuthentication.authenticated(apikeyService.resolveApiKey(apiKey));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return ApiKeyAuthentication.class.isAssignableFrom(authentication);
	}
}
