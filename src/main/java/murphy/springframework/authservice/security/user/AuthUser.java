package murphy.springframework.authservice.security.user;

import java.util.List;

import murphy.springframework.authservice.common.Role;

public record AuthUser(String userId, List<Role> roles) {}
