package murphy.springframework.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import murphy.springframework.authservice.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByUsername(String username);
}
