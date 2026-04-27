package murphy.springframework.authservice.bootstrap;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import murphy.springframework.authservice.common.ApiKeyScope;
import murphy.springframework.authservice.common.Role;
import murphy.springframework.authservice.entity.ApiKeyEntity;
import murphy.springframework.authservice.entity.UserEntity;
import murphy.springframework.authservice.repository.ApiKeyRepository;
import murphy.springframework.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

	private final UserRepository userRepository;
	private final ApiKeyRepository apiKeyRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${app.bootstrap.enabled}")
	private boolean enableBootstrapData;

	@Value("${api-key.prefix-length}")
	private int prefixLength;

	@Value("${api-key.expired-at-seconds}")
	private int expiredAtSeconds;

	private void loadUsers() {
		if (userRepository.count() == 0) {
			UserEntity admin1 = UserEntity.builder() //
					.username("admin") //
					.email("admin@email.com") //
					.roles(Set.of(Role.ROLE_ADMIN)) //
					.passwordHash(passwordEncoder.encode("admin1234")) //
					.active(true) //
					.build();

			UserEntity user1 = UserEntity.builder() //
					.username("user1") //
					.email("user@email.com") //
					.roles(Set.of(Role.ROLE_USER)) //
					.passwordHash(passwordEncoder.encode("user1234")) //
					.active(true) //
					.build();

			userRepository.saveAll(List.of(admin1, user1));
		}
	}

	private void loadApiKeys() {
		if (apiKeyRepository.count() == 0) {
			final String app1_apikey =  "PerFin:this_is_a_super_long_secret_api_key";
			// TODO: Hide key hash
			ApiKeyEntity app1 = ApiKeyEntity.builder() //
					.appName("Personal_Finance") //
					.keyPrefix(app1_apikey.substring(0, prefixLength)) //
					.keyHash(passwordEncoder.encode(app1_apikey)) //
					.description("Personal finance app to let user handle their finances") //
					.scopes(Set.of(ApiKeyScope.ADMIN)) //
					.rateLimit(1000) //
					.expireAt(Instant.now().plusSeconds(expiredAtSeconds)) //
					.createdBy(userRepository.findByUsername("user1").get().getId()) //
					.active(true) //
					.build();
			final String app2_apikey =  "ProSer:this_is_another_super_long_secret_api_key";
			ApiKeyEntity app2 = ApiKeyEntity.builder() //
					.appName("Profile_Service") //
					.keyPrefix(app2_apikey.substring(0, prefixLength)) //
					.keyHash(passwordEncoder.encode(app2_apikey)) //
					.description("Service to store the profile information of users") //
					.scopes(Set.of(ApiKeyScope.READ,  ApiKeyScope.WRITE, ApiKeyScope.DELETE)) //
					.rateLimit(1000) //
					.expireAt(Instant.now().plusSeconds(expiredAtSeconds)) // 90 days
					.createdBy(userRepository.findByUsername("user1").get().getId()) //
					.active(true) //
					.build();

			apiKeyRepository.saveAll(List.of(app1, app2));
		}
	}

	@Override
	public void run(String... args) throws Exception {
		if(enableBootstrapData) {
			loadUsers();
			loadApiKeys();
		}
	}
}
