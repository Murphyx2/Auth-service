package murphy.springframework.authservice.security.service.apikey;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.entity.ApiKeyEntity;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.security.service.apikey.specification.ApikeySpecs;
import murphy.springframework.authservice.security.user.AuthApp;

@Service
public class ApikeyService {

	private final ApiKeyRepository apiKeyRepository;
	private final PasswordEncoder passwordEncoder;

	public ApikeyService(ApiKeyRepository apiKeyRepository, //
			PasswordEncoder passwordEncoder) {
		this.apiKeyRepository = apiKeyRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public AuthApp resolveApiKey(String apiKey) {

		Map<String, String> prefixedApiKey = ApikeyGeneratorUtils.extractPrefixApikey(apiKey);

		if(prefixedApiKey.isEmpty()) {
			throw new BadCredentialsException("Invalid ApiKey");
		}

		Specification<ApiKeyEntity> spec = ApikeySpecs //
				.isValidNowWithPrefix(prefixedApiKey.get("prefix"));
		Optional<ApiKeyEntity> apikeyEntity = apiKeyRepository.findOne(spec);
		if (apikeyEntity.isEmpty()) {
			throw new BadCredentialsException("Api key is invalid or revoked");
		}

		if (!passwordEncoder.matches(prefixedApiKey.get("apikey"), apikeyEntity.get().getKeyHash())) {
			throw new BadCredentialsException("Api key is invalid or revoked");
		}

		String clientId = apikeyEntity.get().getId().toString();
		return new AuthApp(clientId, apikeyEntity.get().getScopes());
	}
}
