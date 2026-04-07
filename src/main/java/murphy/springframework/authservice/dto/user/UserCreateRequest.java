package murphy.springframework.authservice.dto.user;

public record UserCreateRequest (
		String username,
		String password,
		String firstname,
		String lastname
){
}
