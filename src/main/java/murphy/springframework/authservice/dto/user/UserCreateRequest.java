package murphy.springframework.authservice.dto.user;

public record UserCreateRequest (
		String username,
		String password,
		String email,
		String firstname,
		String lastname
){
}
