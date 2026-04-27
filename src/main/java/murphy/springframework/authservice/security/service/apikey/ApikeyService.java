package murphy.springframework.authservice.security.service.apikey;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppRequest;
import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppResponse;
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

	@Value("${api-key.rate-limit}")
	private int defaultRateLimit;

	public ApikeyService(ApiKeyRepository apiKeyRepository, //
			PasswordEncoder passwordEncoder) {
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

	public ApiKeyCreateAppResponse createAppRegister(ApiKeyCreateAppRequest request){

		//Check, Is there an app with the same name?
		if(apiKeyRepository.findByAppName(request.appName()).isPresent()){
			throw new ApplicationAuthenticationException(String.format("App of name %s already exists", request.appName()));
		}

		final String generatedApiKey =  ApikeyGeneratorUtils.generateApikey();
		String apikeyWithPrefix = ApikeyGeneratorUtils.generatePrefixToApikey(request.appName(), generatedApiKey);
		String shortPrefix = apikeyWithPrefix.substring(0, prefixLength+request.appName().length());

		ApiKeyEntity apiKeyEntity =  ApiKeyEntity.builder()
				.appName(request.appName()) //
				.description(request.description()) //
				.keyHash(passwordEncoder.encode(generatedApiKey)) //
				.keyPrefix(shortPrefix) //
				.active(true) //
				.rateLimit(defaultRateLimit) //
				.build();

		apiKeyEntity.addScope(request.apiKeyScopes());

		if(request.expiredAt() != null){
			apiKeyEntity.setExpireAt(request.expiredAt());
		}
		if(request.rateLimit() != null){
			apiKeyEntity.setRateLimit(request.rateLimit());
		}

		apiKeyRepository.save(apiKeyEntity);

		return new ApiKeyCreateAppResponse(apikeyWithPrefix);
	}
}
