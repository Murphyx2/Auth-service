package murphy.springframework.authservice.mapper;

import murphy.springframework.authservice.dto.user.UserCreateRequest;
import murphy.springframework.authservice.dto.user.UserResponse;
import murphy.springframework.authservice.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(config = AppMapperConfig.class)
public interface UserMapper {

	UserEntity userCreateRequestToUserEntity(UserCreateRequest userCreateRequest);

	UserResponse toResponse(UserEntity userEntity);
}
