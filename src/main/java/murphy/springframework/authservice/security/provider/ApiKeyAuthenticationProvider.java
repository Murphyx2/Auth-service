package murphy.springframework.authservice.security.provider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import murphy.springframework.authservice.common.Role;
import murphy.springframework.authservice.config.properties.ApiKeyClientsProperties;
import murphy.springframework.authservice.security.authentication.ApiKeyAuthentication;
import murphy.springframework.authservice.security.user.AuthUser;
import murphy.springframework.authservice.security.user.AuthUserType;
import org.jspecify.annotations.Nullable;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

	private final Map<String, String> apiKeysToClientIds;

	public ApiKeyAuthenticationProvider(ApiKeyClientsProperties apiKeyClientsProperties) { //
		this.apiKeysToClientIds = apiKeyClientsProperties.getClients() //
				.entrySet().stream() //
				.collect(Collectors.toMap( //
						Map.Entry::getValue, Map.Entry::getKey, (oldValue, newValue) -> oldValue //
				));
	}

	@Override
	public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {

		ApiKeyAuthentication apiKeyAuthentication = (ApiKeyAuthentication) authentication;

		String apiKey = apiKeyAuthentication.apiKey();

		if(!apiKeysToClientIds.containsKey(apiKey)) {
			throw new BadCredentialsException("Invalid api key");
		}

		String clientId = apiKeysToClientIds.get(apiKey);
		AuthUser authUser = new AuthUser(clientId, List.of(Role.ROLE_ADMIN), AuthUserType.APPLICATION);
		return ApiKeyAuthentication.authenticated(authUser);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return ApiKeyAuthentication.class.isAssignableFrom(authentication);
	}
}
