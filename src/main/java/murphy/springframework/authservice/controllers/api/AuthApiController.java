package murphy.springframework.authservice.controllers.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppRequest;
import murphy.springframework.authservice.dto.apikey.ApiKeyCreateAppResponse;
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/app_register")
	public ApiKeyCreateAppResponse registerAppWithApikey(@RequestBody ApiKeyCreateAppRequest apiKeyCreateAppRequest) {

		return authService //
				.createAppRegister(apiKeyCreateAppRequest);
	}

}
