package murphy.springframework.authservice.security.service.apikey;

import java.util.Map;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ApikeyGeneratorUtilsTest {

	String apikey;
	String appName = "myTestApp";

	@BeforeEach
	void setUp() {
		apikey = ApikeyGeneratorUtils.generateApikey();
	}

	@Test
	void testGenerateApikeySuccess() {

		assertNotNull(apikey);
		assertFalse(apikey.isEmpty());
	}

	@Test
	void testGeneratePrefixToApikeySuccess() {
		String expectedPrefix = "app_"+appName.toLowerCase();
		String prefixedApikey = ApikeyGeneratorUtils.generatePrefixToApikey(appName, apikey);

		assertNotNull(prefixedApikey);
		assertFalse(prefixedApikey.isEmpty());
		assertEquals(2, prefixedApikey.split("-").length);
		assertEquals(expectedPrefix, prefixedApikey.split("-")[0]);
		assertEquals(apikey, prefixedApikey.split("-")[1]);
	}

	@Test
	void testGeneratePrefixToApikeyEmptyParameters() {

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ApikeyGeneratorUtils.generatePrefixToApikey("","");
		});

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ApikeyGeneratorUtils.generatePrefixToApikey(appName,"");
		});

		Assertions.assertThrows(NullPointerException.class, () -> {
			ApikeyGeneratorUtils.generatePrefixToApikey(null,null);
		});

		Assertions.assertThrows(NullPointerException.class, () -> {
			ApikeyGeneratorUtils.generatePrefixToApikey(appName,null);
		});

		Assertions.assertThrows(NullPointerException.class, () -> {
			ApikeyGeneratorUtils.generatePrefixToApikey(null,apikey);
		});
	}

	@Test
	void testExtractPrefixApikeySuccess() {
		String prefixedApp =  ApikeyGeneratorUtils.generatePrefixToApikey(appName, apikey);
		String expectedPrefix = "app_"+appName.toLowerCase();
		Map<String, String> apikeyElements =  ApikeyGeneratorUtils.extractPrefixApikey(prefixedApp);

		assertEquals(expectedPrefix, apikeyElements.get("prefix"));
		assertEquals(apikey, apikeyElements.get("apikey"));
	}

	@Test
	void testExtractPrefixApikeyIllegalArgument() {

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ApikeyGeneratorUtils.extractPrefixApikey(null);
		});

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ApikeyGeneratorUtils.extractPrefixApikey("");
		});

		Map<String, String> apikeyMap = ApikeyGeneratorUtils //
				.extractPrefixApikey("MadeUpApikey");
		Assertions.assertTrue(apikeyMap.isEmpty());
	}

}