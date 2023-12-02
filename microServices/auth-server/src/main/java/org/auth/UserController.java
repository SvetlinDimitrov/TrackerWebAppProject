package org.auth;

import org.auth.config.security.JwtUtil;
import org.auth.exceptions.ExpiredTokenException;
import org.auth.exceptions.UserNotFoundException;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.ErrorResponse;
import org.auth.model.dto.ErrorSingleResponse;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.LoginUserDto;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.services.UserServiceImp;
import org.auth.services.UserServiceKafkaImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTDecodeException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserServiceImp userServiceImp;
    private final UserServiceKafkaImp userServiceKafkaImp;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> createUserAccount(@Valid @RequestBody RegisterUserDto registerUserDto,
            BindingResult result) throws WrongUserCredentialsException {

        if (result.hasErrors()) {
            throw new WrongUserCredentialsException(result.getFieldErrors());
        }
        userServiceKafkaImp.register(registerUserDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenView> login(@RequestBody LoginUserDto userDto) throws WrongUserCredentialsException {

        UserView userView = userServiceImp.login(userDto);

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
    }

    @GetMapping(path = "/details")
    public ResponseEntity<UserView> getUserDetails(@RequestHeader("Authorization") String authorization)
            throws ExpiredTokenException {

        UserView userView = jwtUtil.extractUserId(authorization)
                .map(id -> userServiceImp.getUserViewById(id))
                .orElseThrow(() -> new ExpiredTokenException("Token is expiated"));

        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<JwtTokenView> editUserProfile(@RequestBody EditUserDto dto,
            @RequestHeader("Authorization") String authorization) throws ExpiredTokenException {

        UserView userView = jwtUtil.extractUserId(authorization)
                .map(id -> userServiceKafkaImp.editUserEntity(dto, id))
                .orElseThrow(() -> new ExpiredTokenException("Token is expiated"));

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);

    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(@RequestHeader("Authorization") String authorization)
            throws ExpiredTokenException, UserNotFoundException {

        Long userId = jwtUtil.extractUserId(authorization)
                .orElseThrow(() -> new ExpiredTokenException("Token is expiated"));

        userServiceKafkaImp.deleteUserById(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(WrongUserCredentialsException.class)
    public ResponseEntity<ErrorResponse> wrongCredentialsErrorCaught(WrongUserCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessageWithErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorSingleResponse> catchExpiredTokenException(ExpiredTokenException e) {
        return new ResponseEntity<>(new ErrorSingleResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorSingleResponse> wrongTokenErrorCaught(UserNotFoundException e) {
        return new ResponseEntity<>(new ErrorSingleResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
