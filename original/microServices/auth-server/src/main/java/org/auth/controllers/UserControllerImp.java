package org.auth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.auth.config.security.JwtUtil;
import org.auth.model.dto.AuthRequestDto;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.UserCreateRequest;
import org.auth.model.dto.UserEditRequest;
import org.auth.model.dto.UserView;
import org.auth.services.UserServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImp implements UserController {

    private final JwtUtil jwtUtil;
    private final UserServiceImp userServiceImp;

    @Override
    public ResponseEntity<HttpStatus> create(
        @RequestBody @Valid UserCreateRequest registerUserDto) {
        userServiceImp.register(registerUserDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JwtTokenView> login(@RequestBody @Valid AuthRequestDto userDto) {
        UserView userView = userServiceImp.login(userDto);

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
    }

    public ResponseEntity<UserView> me() {
        return new ResponseEntity<>(userServiceImp.me(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JwtTokenView> edit(UserEditRequest dto) {
        UserView userView = userServiceImp.edit(dto);

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> delete() {
        userServiceImp.delete();

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
