package murphy.springframework.authservice.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "api-key")
@Data
public class ApiKeyClientsProperties {

	private Map<String, String> clients;
}
