package murphy.springframework.authservice.service;

import org.springframework.stereotype.Service;

import murphy.springframework.authservice.dto.user.UserResponseWithCredentials;
import murphy.springframework.authservice.entity.UserEntity;
import murphy.springframework.authservice.exception.NotFoundException;
import murphy.springframework.authservice.mapper.UserMapper;
import murphy.springframework.authservice.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public UserResponseWithCredentials getUserCredentialsByUsername(String username) {

		UserEntity userEntity =
				userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
		return new UserResponseWithCredentials(
				userMapper.toResponse(userEntity), userEntity.getPasswordHash());
	}
}
