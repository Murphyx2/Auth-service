package murphy.springframework.authservice.dto.apikey;

import java.time.Instant;
import java.util.Set;

import murphy.springframework.authservice.common.ApiKeyScope;

public record ApiKeyCreateAppRequest(String appName,
                                     Set<ApiKeyScope> apiKeyScopes,
                                     String description,
									 Integer rateLimit,
                                     Instant expiredAt) {
}
