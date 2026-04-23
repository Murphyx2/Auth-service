package murphy.springframework.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import murphy.springframework.authservice.entity.ApiKeyEntity;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, String> {

	Optional<ApiKeyEntity> findByAppName(String appName);

	Optional<ApiKeyEntity> findByKeyPrefix(String keyPrefix);
}
