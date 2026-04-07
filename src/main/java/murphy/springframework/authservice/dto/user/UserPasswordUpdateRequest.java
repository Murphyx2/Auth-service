package murphy.springframework.authservice.dto.user;

public record UserPasswordUpdateRequest(String oldPassword, String newPassword) {
}
