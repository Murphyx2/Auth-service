package murphy.springframework.authservice.dto.user;

import java.util.List;

import murphy.springframework.authservice.common.Role;

public record UserResponse(
		String id,
		String username,
		String firstname,
		String lastname,
		String email,
		List<Role> roles,
		Boolean active) {}
