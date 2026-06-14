package murphy.springframework.authservice.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppRequest;
import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppResponse;
import murphy.springframework.authservice.dto.user.UserResponse;
import murphy.springframework.authservice.dto.user.UserResponseWithCredentials;
import murphy.springframework.authservice.entity.ApiKeyEntity;
import murphy.springframework.authservice.exception.NotFoundException;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.security.dto.LoginDto;
import murphy.springframework.authservice.security.dto.TokenDto;
import murphy.springframework.authservice.security.exception.ApplicationAuthenticationException;
import murphy.springframework.authservice.security.service.apikey.ApikeyGeneratorUtils;
import murphy.springframework.authservice.security.service.jwt.JwtService;
import murphy.springframework.authservice.security.user.AuthUser;
import murphy.springframework.authservice.security.user.AuthUserType;

import static murphy.springframework.authservice.utils.SecurityUtils.getCurrentUser;

@Service
public class AuthService {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final ApiKeyRepository apiKeyRepository;

	@Value("${api-key.prefix-length}")
	private int prefixLength;

	@Value("${api-key.rate-limit}")
	private int defaultRateLimit;

	public AuthService(JwtService jwtService, //
			UserService userService, //
			PasswordEncoder passwordEncoder, ApiKeyRepository apiKeyRepository) {
		this.jwtService = jwtService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.apiKeyRepository = apiKeyRepository;
	}

	public ApiKeyCreateAppResponse createAppRegister(ApiKeyCreateAppRequest request) {

		//Check, Is there an app with the same name?
		if (apiKeyRepository.findByAppName(request.appName()).isPresent()) {
			throw new ApplicationAuthenticationException(String.format("App of name %s already exists", request.appName()));
		}

		AuthUser authUser = getCurrentUser();

		final String generatedApiKey = ApikeyGeneratorUtils.generateApikey();
		String apikeyWithPrefix = ApikeyGeneratorUtils.generatePrefixToApikey(request.appName(), generatedApiKey);
		String shortPrefix = apikeyWithPrefix.substring(0, prefixLength + request.appName().length());

		ApiKeyEntity apiKeyEntity = ApiKeyEntity.builder().appName(request.appName()) //
				.description(request.description()) //
				.keyHash(passwordEncoder.encode(generatedApiKey)) //
				.keyPrefix(shortPrefix) //
				.active(true) //
				.rateLimit(defaultRateLimit) //
				.createdBy(UUID.fromString(authUser.userId())) //
				.build();

		apiKeyEntity.addScope(request.apiKeyScopes());

		if (request.expiredAt() != null) {
			apiKeyEntity.setExpireAt(request.expiredAt());
		}
		if (request.rateLimit() != null) {
			apiKeyEntity.setRateLimit(request.rateLimit());
		}

		apiKeyRepository.save(apiKeyEntity);

		return new ApiKeyCreateAppResponse(apikeyWithPrefix);
	}

	public TokenDto login(LoginDto loginDto) {
		UserResponseWithCredentials userCredentials;
		try {
			userCredentials = userService //
					.getUserCredentialsByUsername(loginDto.username());
		} catch (NotFoundException e) {
			userCredentials = userService.getUserCredentialsByUsernameInMemory(loginDto.username());
		}

		if (!passwordEncoder.matches(loginDto.password(), userCredentials.passwordHash())) {
			throw new ApplicationAuthenticationException("Password is incorrect");
		}

		UserResponse userResponse = userCredentials.userResponse();
		AuthUser authUser = new AuthUser(userResponse.id(), userResponse.roles(), AuthUserType.INTERNAL);

		String jwtToken = jwtService.createJwtToken(authUser);

		return new TokenDto(jwtToken);
	}
}
