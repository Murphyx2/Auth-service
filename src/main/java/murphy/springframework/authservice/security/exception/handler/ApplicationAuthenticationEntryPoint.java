package murphy.springframework.authservice.security.exception.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import murphy.springframework.authservice.dto.error.ApiErrorResponse;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class ApplicationAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	public ApplicationAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, //
			HttpServletResponse response,  //
			AuthenticationException authException) throws IOException, ServletException {

		log.error("Authentication exception occurred for request: {}", request, authException);

		ApiErrorResponse apiErrorResponse = new ApiErrorResponse(authException.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		objectMapper.writeValue(response.getOutputStream(), apiErrorResponse);
	}
}
