package org.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.auth.config.security.UserPrincipal;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.config.security.JwtUtil;
import org.auth.model.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserServiceImp userServiceImp;


    @PostMapping("/register")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody RegisterUserDto registerUserDto,
                                                    BindingResult result) throws WrongUserCredentialsException {

        if (result.hasErrors()) {
            throw new WrongUserCredentialsException(result.getFieldErrors());
        }
        userServiceImp.register(registerUserDto);

        return new ResponseEntity<>("Successfuly created account", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenView> login(@RequestBody LoginUserDto userDto) throws WrongUserCredentialsException {

        UserView userView = userServiceImp.login(userDto);

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
    }


    @GetMapping(path = "/details")
    public ResponseEntity<UserView> getUserDetails(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserView userView = userServiceImp.getUserViewById(userPrincipal.getId());
        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<JwtTokenView> editUserProfile(@RequestBody EditUserDto userDto,
                                                    @RequestHeader("X-ViewUser") String userToken) {

        UserView userView = userServiceImp.editUserEntity(userDto, userToken);

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);

    }

    @ExceptionHandler(WrongUserCredentialsException.class)
    public ResponseEntity<ErrorResponse> wrongCredentialsErrorCaught(WrongUserCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessageWithErrors()), HttpStatus.BAD_REQUEST);
    }
}
