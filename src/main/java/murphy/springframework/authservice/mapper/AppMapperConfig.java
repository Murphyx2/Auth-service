package murphy.springframework.authservice.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MapperConfig;

@MapperConfig(
		componentModel = ComponentModel.SPRING,
		injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AppMapperConfig {
}
