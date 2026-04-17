package murphy.springframework.authservice.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.val;
import murphy.springframework.authservice.common.Role;
import murphy.springframework.authservice.dto.user.UserCreateRequest;
import murphy.springframework.authservice.dto.user.UserResponse;
import murphy.springframework.authservice.dto.user.UserResponseWithCredentials;
import murphy.springframework.authservice.entity.UserEntity;
import murphy.springframework.authservice.exception.NotFoundException;
import murphy.springframework.authservice.exception.UserAlreadyExistsException;
import murphy.springframework.authservice.mapper.UserMapper;
import murphy.springframework.authservice.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserDetailsService userDetailsService;

	public UserService(UserRepository userRepository //
			, UserMapper userMapper //
			, UserDetailsService userDetailsService) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.userDetailsService = userDetailsService;
	}

	public UserResponse createUser(UserCreateRequest createRequest) {

		if(userRepository.findByUsername(createRequest.username()).isPresent() &&
		userRepository.findByEmail(createRequest.email()).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		return userMapper //
				.toResponse(userRepository //
						.save(userMapper //
								.userCreateRequestToUserEntity(createRequest) //
						) //
				);

	}

	public UserResponseWithCredentials getUserCredentialsByUsername(String username) {

		UserEntity userEntity =
				userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
		return new UserResponseWithCredentials(
				userMapper.toResponse(userEntity), userEntity.getPasswordHash());
	}

	public UserResponseWithCredentials getUserCredentialsByUsernameInMemory(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		var userResponse = new UserResponse("1", userDetails.getUsername(), "","", "",
				userDetails.getAuthorities().stream().map(grantedAuthority ->
						Role.valueOf(grantedAuthority.getAuthority()) //
				).toList(), userDetails.isEnabled());

		return new  UserResponseWithCredentials(userResponse, userDetails.getPassword());
	}
}
