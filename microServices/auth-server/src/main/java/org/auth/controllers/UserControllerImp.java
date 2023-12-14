package org.auth.controllers;

import org.auth.config.security.JwtUtil;
import org.auth.exceptions.UserNotFoundException;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.LoginUserDto;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.services.UserServiceImp;
import org.auth.services.UserServiceKafkaImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserControllerImp implements UserController {

    private final JwtUtil jwtUtil;
    private final UserServiceImp userServiceImp;
    private final UserServiceKafkaImp userServiceKafkaImp;

    @Override
    public ResponseEntity<HttpStatus> createUserAccount(
            @Valid RegisterUserDto registerUserDto,
            BindingResult result)
            throws WrongUserCredentialsException {
        if (result.hasErrors()) {
            throw new WrongUserCredentialsException(result.getFieldErrors());
        }
        userServiceKafkaImp.register(registerUserDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JwtTokenView> login(LoginUserDto userDto) throws WrongUserCredentialsException {
        UserView userView = userServiceImp.login(userDto);

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserView> getUserDetails(String authorization) {
        UserView userView = userServiceImp.getUserViewById(jwtUtil.extractUserId(authorization));

        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JwtTokenView> editUserProfile(EditUserDto dto, String authorization)
            throws UserNotFoundException {
        UserView userView = userServiceKafkaImp.editUserEntity(dto, jwtUtil.extractUserId(authorization));

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteUser(String authorization) throws UserNotFoundException {
        userServiceKafkaImp.deleteUserById(jwtUtil.extractUserId(authorization));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
