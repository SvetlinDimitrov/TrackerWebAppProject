package org.auth.features.user.services;

import org.auth.features.user.dto.UserCreateRequest;
import org.example.domain.user.dto.UserEditRequest;
import org.example.domain.user.dto.UserView;
import org.auth.features.user.entity.User;

public interface UserService {

  UserView getById(String userId);

  UserView create(UserCreateRequest userDto);

  UserView edit(UserEditRequest userDto, String userId);

  void delete(String userId);

  User findByEmail(String email);

  UserView me();
}
