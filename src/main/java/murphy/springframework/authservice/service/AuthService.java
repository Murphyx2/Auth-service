package murphy.springframework.authservice.service;

import java.util.UUID;

import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.dto.user.UserResponse;
import murphy.springframework.authservice.dto.user.UserResponseWithCredentials;
import murphy.springframework.authservice.security.dto.LoginDto;
import murphy.springframework.authservice.security.dto.TokenDto;
import murphy.springframework.authservice.security.exception.ApplicationAuthenticationException;
import murphy.springframework.authservice.security.user.AuthUser;
import murphy.springframework.authservice.security.user.AuthUserCache;

@Service
public class AuthService {

	private final AuthUserCache authUserCache;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public AuthService(AuthUserCache authUserCache, //
			UserService userService, //
			PasswordEncoder passwordEncoder) {
		this.authUserCache = authUserCache;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	public TokenDto login(LoginDto loginDto) {
		UserResponseWithCredentials userCredentials = userService.getUserCredentialsByUsername(loginDto.username());

		if(!passwordEncoder.matches(loginDto.password(), userCredentials.passwordHash())) {
			throw new ApplicationAuthenticationException("Password is incorrect");
		}

		String token = UUID.randomUUID().toString();
		UserResponse userResponse = userCredentials.userResponse();
		AuthUser authUser = new AuthUser(userResponse.id(), userResponse.roles());

		authUserCache.login(token, authUser);

		return new TokenDto(token);
	}

	public void logout(String token) {

		authUserCache.logout(token);
	}
}
