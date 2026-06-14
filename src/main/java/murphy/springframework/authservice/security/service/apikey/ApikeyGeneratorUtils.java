package murphy.springframework.authservice.security.service.apikey;

import org.springframework.beans.factory.annotation.Value;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import jakarta.annotation.PostConstruct;

public class ApikeyGeneratorUtils {

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private static final int KEY_LENGTH_BYTES = 32;
	private static final String REGEX = "[^a-z0-9]";
	private static final String API_KEY_PREFIX_TEMPLATE = "app_"+"%s"+"-"+"%s";
	private static int prefixLength;

	@Value("${api-key.prefix-length}")
	private int injectedPrefixLength;

	private ApikeyGeneratorUtils(){
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	@PostConstruct
	private void init() {
		prefixLength = injectedPrefixLength;
	}

	public static String generateApikey() {
		byte[] apikeyBytes = new byte[KEY_LENGTH_BYTES];
		SECURE_RANDOM.nextBytes(apikeyBytes);
		return Base64.getEncoder().encodeToString(apikeyBytes);
	}

	public static String generatePrefixToApikey(String appName, String apikey) throws NullPointerException, IllegalArgumentException {
		if(appName ==  null || apikey == null){
			throw new NullPointerException("appName or apikey is null");
		}
		if (appName.isEmpty() || apikey.isEmpty()) {
			throw new IllegalArgumentException("appName or apikey is empty");
		}

		if(apikey.length() < prefixLength){
			throw new IllegalArgumentException("apikey length less than "+ prefixLength);
		}

		return String //
				.format(API_KEY_PREFIX_TEMPLATE, //
						appName.toLowerCase().replaceAll(REGEX, ""), //
						apikey);
	}

	public static Map<String, String> extractPrefixApikey(String prefixedApikey){

		if(prefixedApikey == null ||  prefixedApikey.isEmpty()){
			throw new IllegalArgumentException("prefixedApikey is null or empty");
		}
		try {
			String[] splitElement = prefixedApikey.split("-");
			return Map.of("prefix", splitElement[0], "apikey", splitElement[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return Collections.emptyMap();
		}
	}
}
