package org.auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.*;
import org.auth.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserServiceImp userServiceImp;


    @PostMapping(path = "/register" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUserAccount(@RequestBody @Valid RegisterUserDto registerUserDto,
                                                    BindingResult result) throws WrongUserCredentialsException {

        if(result.hasErrors()){
            throw new WrongUserCredentialsException(result.getFieldErrors());
        }
        userServiceImp.register(registerUserDto);

        return new ResponseEntity<>("Successfuly created account" , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenView> login (LoginUserDto userDto) throws WrongUserCredentialsException {

        UserView userView = userServiceImp.login(userDto);

        return new ResponseEntity<>(jwtUtil.createJwtToken(userView) , HttpStatus.OK);
    }


    @GetMapping("/details/{userId}")
    public ResponseEntity<UserView> getUserDetails(@PathVariable Long userId){
        UserView userView = userServiceImp.getUserViewById(userId);
        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    @PatchMapping("/edit/{userId}")
    public ResponseEntity<UserView> editUserProfile(@RequestBody EditUserDto userDto ,
                                                    @PathVariable Long userId){

        //TODO: CORECT THIS TO GET THE USERiD FROM THE JWT TOKEN AND THEN SEND NEW JWT TOKEN
        userServiceImp.editUserEntity(userDto , userId);
        UserView userView = userServiceImp.getUserViewById(userId);

        return new ResponseEntity<>(userView , HttpStatus.ACCEPTED);

    }

    @ExceptionHandler(WrongUserCredentialsException.class)
    public ResponseEntity<ErrorResponse> wrongCredentialsErrorCaught(WrongUserCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessageWithErrors()), HttpStatus.BAD_REQUEST);
    }
}
