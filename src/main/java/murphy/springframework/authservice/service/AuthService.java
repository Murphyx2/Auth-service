package murphy.springframework.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.security.dto.LoginDto;
import murphy.springframework.authservice.security.dto.TokenDto;
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
		UserResponseWithCredentials userCredentials = userService.getUserCredentialsByUserName(loginDto.username());
	}

	public void logout(String token) {

	}
}
