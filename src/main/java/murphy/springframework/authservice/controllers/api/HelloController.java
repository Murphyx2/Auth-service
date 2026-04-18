package murphy.springframework.authservice.controllers.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/about")
	public String about() {
		return "This is a public endpoint, no login needed";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping({ "/hello", "/" })
	public String hello() {
		return "Hello, You are authenticated";
	}
}
