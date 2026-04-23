package murphy.springframework.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import murphy.springframework.authservice.security.filter.ApiKeyFilter;
import murphy.springframework.authservice.security.filter.JwtFilter;
import murphy.springframework.authservice.security.filter.SecurityAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final ApiKeyFilter apiKeyFilter;
	private final SecurityAuthenticationFilter  securityAuthenticationFilter;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final AccessDeniedHandler accessDeniedHandler;
	private final PasswordEncoder passwordEncoder;

	public SecurityConfig(JwtFilter jwtFilter, ApiKeyFilter apiKeyFilter, SecurityAuthenticationFilter securityAuthenticationFilter, //
			AuthenticationEntryPoint authenticationEntryPoint,  //
			AccessDeniedHandler accessDeniedHandler, PasswordEncoder passwordEncoder) {
		this.jwtFilter = jwtFilter;
		this.apiKeyFilter = apiKeyFilter;
		this.securityAuthenticationFilter = securityAuthenticationFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.accessDeniedHandler = accessDeniedHandler;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.addFilterBefore(securityAuthenticationFilter, AuthorizationFilter.class)
				.addFilterBefore(jwtFilter, SecurityAuthenticationFilter.class)
				.addFilterBefore(apiKeyFilter, JwtFilter.class)
				.authorizeHttpRequests(
						matcher ->
								matcher
										// method security will be evaluated after DSL configs,
										// so we have to define public paths upfront
										.requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/users")
										.permitAll())
				.authorizeHttpRequests(matcher -> matcher.anyRequest().authenticated())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(
						configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(
						customizer ->
								customizer
										.accessDeniedHandler(accessDeniedHandler)
										.authenticationEntryPoint(authenticationEntryPoint));

		return http.build();
	}

	// register NoOp AuthenticationManager to avoid log printed by default autoconfiguration
	@Bean
	public AuthenticationManager noOpAuthenticationManager() {
		return authentication -> null;
	}


	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User
				.withUsername("user") //
				.password(passwordEncoder.encode("password")) //
				.roles("ADMIN") //
				.build();
		return new InMemoryUserDetailsManager(userDetails);
	}

}
