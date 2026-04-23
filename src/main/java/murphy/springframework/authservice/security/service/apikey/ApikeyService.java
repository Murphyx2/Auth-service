package murphy.springframework.authservice.security.service.apikey;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.common.ApiKeyScope;
import murphy.springframework.authservice.entity.ApiKeyEntity;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.security.user.AuthApp;

@Service
public class ApikeyService {

	private final ApiKeyRepository apiKeyRepository;

	public ApikeyService(ApiKeyRepository apiKeyRepository) {
		this.apiKeyRepository = apiKeyRepository;
	}

	public AuthApp resolveApiKey(String apiKey){

		String apikeyPrefix = apiKey.substring(7);
		//TODO: Add specification to filter only valid apikeys
		Optional<ApiKeyEntity> apikeyEntity = apiKeyRepository.findByKeyPrefix(apikeyPrefix);
		if (apikeyEntity.isEmpty()) {
			throw new BadCredentialsException("Invalid api key or revoked apikey");
		}

		String clientId = apikeyEntity.get().getId().toString();
		AuthApp authApp = new AuthApp(clientId, Set.of(ApiKeyScope.ADMIN));

		return authApp;
	}
}
