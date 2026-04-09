package murphy.springframework.authservice.mapper;

import murphy.springframework.authservice.dto.user.UserResponse;
import murphy.springframework.authservice.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserResponse toResponse(UserEntity userEntity);
}
