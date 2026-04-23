package murphy.springframework.authservice.security.service.apikey.specification;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

import murphy.springframework.authservice.entity.ApiKeyEntity;

public class ApikeySpecs {

	private ApikeySpecs(){
		// Empty on purpose
	}

	public static Specification<ApiKeyEntity> isValidNowWithPrefix(String apikeyPrefix){
		Instant now = Instant.now();
		return (root, query, cb) -> cb.and(
				cb.isTrue(root.get("active")),
				cb.greaterThan(root.get("expireAt"),now),
				cb.equal(root.get("keyPrefix"), apikeyPrefix));
	}
}
