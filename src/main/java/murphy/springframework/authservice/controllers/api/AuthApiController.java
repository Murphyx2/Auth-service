package murphy.springframework.authservice.controllers.api;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import murphy.springframework.authservice.common.AuthConstants;
import murphy.springframework.authservice.security.dto.LoginDto;
import murphy.springframework.authservice.security.dto.TokenDto;
import murphy.springframework.authservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

	private final AuthService authService;

	public AuthApiController(AuthService authService) {
		this.authService = authService;
	}

	@PreAuthorize("isAnonymous()")
	@PostMapping("/login")
	public TokenDto login(@RequestBody LoginDto loginDto) {
		return authService.login(loginDto);
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		String token = Optional.ofNullable(request.getHeader(AuthConstants.AUTHORIZATION_HEADER))
				.orElseThrow();

		authService.logout(token);
	}

}
