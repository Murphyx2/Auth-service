package murphy.springframework.authservice.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import murphy.springframework.authservice.common.ApiKeyScope;
import murphy.springframework.authservice.common.Role;
import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppRequest;
import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppResponse;
import murphy.springframework.authservice.entity.ApiKeyEntity;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.security.service.apikey.ApikeyGeneratorUtils;
import murphy.springframework.authservice.security.user.AuthUser;
import murphy.springframework.authservice.security.user.AuthUserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ApiKeyRepository apiKeyRepository;

	@Autowired
	AuthService authService;

	ApiKeyCreateAppRequest createAppRequest;

	String apikey;
	String appName;
	String prefixedApikey;
	String prefix;
	ApiKeyEntity savedApiKeyEntity;
	String testAppName = "MyTestApp2";
	AuthUser testUser;

	@Test
	void createAppRegisterSuccess() {

		ApiKeyCreateAppResponse response = authService.createAppRegister(createAppRequest);

		Assertions.assertNotNull(response);
		Assertions.assertTrue(response.apikey().length() > 10);

		Optional<ApiKeyEntity> registeredApiKey = apiKeyRepository.findByAppName(testAppName);

		Assertions.assertTrue(registeredApiKey.isPresent());
		Assertions.assertEquals(registeredApiKey.get().getAppName(), testAppName);
	}

	@BeforeEach
	void setUp() {
		// Add admin to security context
		testUser = new AuthUser(UUID.randomUUID().toString(), List.of(Role.ROLE_ADMIN), AuthUserType.INTERNAL);

		Authentication authentication = new UsernamePasswordAuthenticationToken(testUser,                    // ← Your Record as principal
				null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		// Create test applications

		apikey = ApikeyGeneratorUtils.generateApikey();
		appName = "MyTestApp";
		prefixedApikey = ApikeyGeneratorUtils.generatePrefixToApikey(appName, apikey);
		prefix = ApikeyGeneratorUtils.extractPrefixApikey(prefixedApikey).get("prefix");

		ApiKeyEntity app1 = ApiKeyEntity.builder() //
				.appName(appName) //
				.keyPrefix(prefix) //
				.keyHash(passwordEncoder.encode(apikey)) //
				.description("App registered for testing purposes") //
				.scopes(Set.of(ApiKeyScope.READ, ApiKeyScope.WRITE)) //
				.rateLimit(1000) //
				.expireAt(Instant.now().plusSeconds(7200000)) //
				.createdBy(UUID.randomUUID()) //
				.active(true) //
				.build();

		savedApiKeyEntity = apiKeyRepository.save(app1);

		Set<ApiKeyScope> apiKeyScopes = Set.of(ApiKeyScope.READ, ApiKeyScope.WRITE);
		createAppRequest = new ApiKeyCreateAppRequest(testAppName, apiKeyScopes, //
				"TestApiKey2",  //
				1000,  //
				Instant.now().plus(Duration.ofDays(10)) //
		);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();   // Clean up
	}

}