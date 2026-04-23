package murphy.springframework.authservice.security.service.apikey;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.entity.ApiKeyEntity;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.security.exception.ApplicationAuthenticationException;
import murphy.springframework.authservice.security.service.apikey.specification.ApikeySpecs;
import murphy.springframework.authservice.security.user.AuthApp;

@Service
public class ApikeyService {

	private final ApiKeyRepository apiKeyRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${api-key.prefix-length}")
	private int prefixLength;

	public ApikeyService(ApiKeyRepository apiKeyRepository, PasswordEncoder passwordEncoder) {
		this.apiKeyRepository = apiKeyRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public AuthApp resolveApiKey(String apiKey){

		String apikeyPrefix = apiKey.substring(0, prefixLength);
		Specification<ApiKeyEntity> spec = ApikeySpecs.isValidNowWithPrefix(apikeyPrefix);

		Optional<ApiKeyEntity> apikeyEntity = apiKeyRepository.findOne(spec);
		if (apikeyEntity.isEmpty()) {
			throw new BadCredentialsException("Api key is invalid or revoked");
		}

		if(!passwordEncoder.matches(apiKey, apikeyEntity.get().getKeyHash())){
			throw new ApplicationAuthenticationException("Api key is invalid or revoked");
		}

		String clientId = apikeyEntity.get().getId().toString();
		return new AuthApp(clientId, apikeyEntity.get().getScopes());
	}
}
