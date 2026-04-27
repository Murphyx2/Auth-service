package murphy.springframework.authservice.common;

import lombok.Getter;

@Getter
public enum ApiKeyScope {
	READ("read"),
	WRITE("write"),
	DELETE("delete"),
	ADMIN("admin"),
	EXECUTE("execute"),
	MANAGE("manage"),
	FULL("full_access");

	private final String value;

	ApiKeyScope(String value) {
		this.value = value;
	}

	public String getAuthority() {
		return value;
	}
}
