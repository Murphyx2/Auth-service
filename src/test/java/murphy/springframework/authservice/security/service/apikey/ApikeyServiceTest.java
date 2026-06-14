package murphy.springframework.authservice.security.service.apikey;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import murphy.springframework.authservice.common.ApiKeyScope;
import murphy.springframework.authservice.entity.ApiKeyEntity;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.security.user.AuthApp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ApikeyServiceTest {

	@Autowired
	ApiKeyRepository apiKeyRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ApikeyService apikeyService;

	String apikey;
	String appName;
	String prefixedApikey;
	String prefix;
	ApiKeyEntity savedApiKeyEntity;

	@BeforeEach
	void setUp() {
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
	}

	@AfterEach
	void tearDown() {
		apiKeyRepository.delete(savedApiKeyEntity);
	}

	@Test
	void testResolveApiKeySuccess() {
		AuthApp authApp = apikeyService.resolveApiKey(prefixedApikey);

		assertNotNull(authApp);
		assertEquals(2, authApp.scopes().size());
		assertEquals(savedApiKeyEntity.getId().toString(), authApp.appId());
		assertTrue(authApp.scopes().contains(ApiKeyScope.READ));
		assertTrue(authApp.scopes().contains(ApiKeyScope.WRITE));
	}

	@Test
	void testResolveApiKeyInvalidApiKey() {
		Assertions.assertThrows(BadCredentialsException.class, () -> {
			apikeyService.resolveApiKey("MadeUpApiKey");
		});

		Assertions.assertThrows(BadCredentialsException.class, () -> {
			apikeyService.resolveApiKey("MadeUpApiKey-Apikeyhere");
		});

		Assertions.assertThrows(BadCredentialsException.class, () -> {
			apikeyService.resolveApiKey(prefix+"-Apikeyhere");
		});
	}
}