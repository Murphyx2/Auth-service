package murphy.springframework.authservice.security.user;

import java.util.Set;

import murphy.springframework.authservice.common.ApiKeyScope;

public record AuthApp(String appId, //
					  Set<ApiKeyScope> scopes //
					  ) {
}
