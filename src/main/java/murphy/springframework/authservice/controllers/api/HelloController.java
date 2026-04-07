package murphy.springframework.authservice.controllers.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/about")
	public String about() {
		return "This is a public endpoint, no login needed";
	}

	@GetMapping({ "/hello", "/" })
	public String hello() {
		return "Hello, You are authenticated";
	}
}
