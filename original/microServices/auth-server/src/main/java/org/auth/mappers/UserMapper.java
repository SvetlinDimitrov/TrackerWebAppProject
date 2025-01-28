package org.auth.mappers;

import org.auth.model.dto.UserCreateRequest;
import org.auth.model.dto.UserEditRequest;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
@DecoratedWith(UserMapperDecoder.class)
public interface UserMapper {

  User toEntity(UserCreateRequest dto);

  UserView toView(User entity);

  void update(@MappingTarget User entity, UserEditRequest dto);
}
