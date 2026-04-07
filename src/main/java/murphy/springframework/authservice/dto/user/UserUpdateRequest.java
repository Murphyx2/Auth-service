package murphy.springframework.authservice.dto.user;

public record UserUpdateRequest(String username, String firstname, String lastname) {
}
