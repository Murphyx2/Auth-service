package murphy.springframework.authservice.bootstrap;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import murphy.springframework.authservice.common.Role;
import murphy.springframework.authservice.entity.UserEntity;
import murphy.springframework.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private boolean bootstrapData = false;

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

	@Override
	public void run(String... args) throws Exception {
		if(bootstrapData) {
			loadUsers();
		}
	}
}
