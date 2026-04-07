package murphy.springframework.authservice.dto.user;

public record UserResponseWithCredentials(UserResponse userResponse, String passwordHash) {
}
