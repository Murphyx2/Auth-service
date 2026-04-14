package murphy.springframework.authservice.service;

import java.util.UUID;

import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import murphy.springframework.authservice.common.Role;
import murphy.springframework.authservice.dto.user.UserResponse;
import murphy.springframework.authservice.dto.user.UserResponseWithCredentials;
import murphy.springframework.authservice.exception.NotFoundException;
import murphy.springframework.authservice.security.dto.LoginDto;
import murphy.springframework.authservice.security.dto.TokenDto;
import murphy.springframework.authservice.security.exception.ApplicationAuthenticationException;
import murphy.springframework.authservice.security.service.jwt.JwtService;
import murphy.springframework.authservice.security.user.AuthUser;

@Service
public class AuthService {


	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	public AuthService(JwtService jwtService, //
			UserService userService, //
			PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}

	public TokenDto login(LoginDto loginDto) {
		UserResponseWithCredentials userCredentials;
		try {
			userCredentials = userService //
					.getUserCredentialsByUsername(loginDto.username());
		} catch (NotFoundException e) {
			userCredentials = userService.getUserCredentialsByUsernameInMemory(loginDto.username());
		}

		if(!passwordEncoder.matches(loginDto.password(), userCredentials.passwordHash())) {
			throw new ApplicationAuthenticationException("Password is incorrect");
		}

		UserResponse userResponse = userCredentials.userResponse();
		AuthUser authUser = new AuthUser(userResponse.id(), userResponse.roles());

		String jwtToken = jwtService.createJwtToken(authUser);

		return new TokenDto(jwtToken);
	}
}
