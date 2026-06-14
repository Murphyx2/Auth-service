package murphy.springframework.authservice.utils;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import murphy.springframework.authservice.common.Role;
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
class SecurityUtilsTest {

	AuthUser testUser;
	UUID testUserId;

	@BeforeEach
	void setUp() {
		testUserId = UUID.randomUUID();
		// Add admin to security context
		testUser = new AuthUser(testUserId.toString(),
				List.of(Role.ROLE_ADMIN),
				AuthUserType.INTERNAL);

		Authentication authentication = new UsernamePasswordAuthenticationToken(testUser,                    // ← Your Record as principal
				null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void getCurrentUserSuccess() {
		AuthUser currentUser = SecurityUtils.getCurrentUser();
		Assertions.assertNotNull(currentUser);
		Assertions.assertEquals(currentUser.userId(), testUserId.toString());
		Assertions.assertEquals(currentUser.roles(), List.of(Role.ROLE_ADMIN));
	}

}