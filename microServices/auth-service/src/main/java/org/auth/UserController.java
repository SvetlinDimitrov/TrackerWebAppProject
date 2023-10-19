package org.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.EditUserDto;
import org.auth.model.dto.LoginUserDto;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.security.JwtUtil;
import org.auth.security.UserDetailsImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nutritionApi/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserServiceImp userServiceImp;


    @PostMapping("/register")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody RegisterUserDto userDto,
                                                    BindingResult result) throws WrongUserCredentialsException {
        if(result.hasErrors()){
            throw new WrongUserCredentialsException(result.getFieldErrors());
        }

        userServiceImp.register(userDto);

        return new ResponseEntity<>("Successfuly created account" , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@Valid @RequestBody LoginUserDto userDto,
                                         BindingResult result) throws WrongUserCredentialsException {

        if(!userServiceImp.login(userDto)){
            result.addError(new FieldError("username_password", "password" , "wrong username or password"));
        }

        if(result.hasErrors()){
            throw new WrongUserCredentialsException(result.getFieldErrors());
        }
        Long userID = userServiceImp.findByEmail(userDto.getEmail()).getId();

        return new ResponseEntity<>(jwtUtil.createJwtToken(userID) , HttpStatus.OK);
    }


    @GetMapping("/details")
    public ResponseEntity<UserView> getUserDetails(Principal principal){
        String email = principal.getName();
        UserView userView = new UserView(userServiceImp.findByEmail(email));
        return new ResponseEntity<>(userView, HttpStatus.OK);
    }

    @PatchMapping("/details")
    public ResponseEntity<UserView> editUserProfile(Principal principal,
                                                    @RequestBody EditUserDto userDto){


        String email = principal.getName();
        User user = userServiceImp.findByEmail(email);
        Long userId = user.getId();

        userServiceImp.editUserEntity(userDto , userId);
        UserView userView = userServiceImp.getUserViewById(userId);

        UserDetailsImp.updateAuthorities();

        return new ResponseEntity<>(userView , HttpStatus.ACCEPTED);

    }

}
