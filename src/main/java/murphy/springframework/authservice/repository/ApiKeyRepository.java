package murphy.springframework.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import murphy.springframework.authservice.entity.ApiKeyEntity;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, String>, //
		JpaSpecificationExecutor<ApiKeyEntity> {

	void deleteByAppName(String appName);

	Optional<ApiKeyEntity> findByAppName(String appName);

	Optional<ApiKeyEntity> findByKeyPrefix(String keyPrefix);
}
