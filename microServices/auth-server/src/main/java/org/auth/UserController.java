package org.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.auth.config.security.JwtUtil;
import org.auth.exceptions.ExpiredTokenException;
import org.auth.exceptions.UserNotFoundException;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.*;
import org.auth.services.UserServiceImp;
import org.auth.services.UserServiceKafkaImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "The User API")
@RequiredArgsConstructor
public class UserController {
    
    private final JwtUtil jwtUtil;
    private final UserServiceImp userServiceImp;
    private final UserServiceKafkaImp userServiceKafkaImp;
    
    @PostMapping("/register")
    @Operation(summary = "Create user account",
        responses = {
            @ApiResponse(responseCode = "201", description = "User account created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterUserDto.class),
                examples = @ExampleObject(name = "registerUserDto", value = "{\"email\":\"test@abv.bg\",\"password\":\"12345\",\"username\":\"test\"}")
            )
        )
    )
    public ResponseEntity<HttpStatus> createUserAccount(@Valid @RequestBody RegisterUserDto registerUserDto,
                                                        BindingResult result) throws WrongUserCredentialsException {
        
        if (result.hasErrors()) {
            throw new WrongUserCredentialsException(result.getFieldErrors());
        }
        userServiceKafkaImp.register(registerUserDto);
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Authenticate user",
        responses = {
            @ApiResponse(responseCode = "200", description = "User authenticated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtTokenView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginUserDto.class),
                examples = @ExampleObject(name = "loginUserDto", value = "{\"email\":\"test@abv.bg\",\"password\":\"12345\"}")
            )
        )
    )
    public ResponseEntity<JwtTokenView> login(@RequestBody LoginUserDto userDto) throws WrongUserCredentialsException {
        
        UserView userView = userServiceImp.login(userDto);
        
        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
    }
    
    
    @GetMapping(path = "/details")
    @Operation(summary = "Get user details",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "User details retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserView.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (appears when spring security take care)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
            
        }
    )
    public ResponseEntity<UserView> getUserDetails(@RequestHeader("Authorization") String authorization) {
        
        UserView userView = userServiceImp.getUserViewById(jwtUtil.extractUserId(authorization));
        
        return new ResponseEntity<>(userView, HttpStatus.OK);
    }
    
    @PatchMapping("/edit")
    @Operation(summary = "Edit user profile",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "User profile edited", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtTokenView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (appears when spring security take care)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
            
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EditUserDto.class),
                examples = @ExampleObject(name = "editUserDto", value = "{\"username\":\"test\",\"kilograms\":70,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\",\"height\":170,\"age\":30}")
            )
        )
    )
    public ResponseEntity<JwtTokenView> editUserProfile(@RequestBody EditUserDto dto,
                                                        @RequestHeader("Authorization") String authorization) throws UserNotFoundException {
        
        UserView userView = userServiceKafkaImp.editUserEntity(dto, jwtUtil.extractUserId(authorization));
        
        return new ResponseEntity<>(jwtUtil.createJwtToken(userView), HttpStatus.OK);
        
    }
    
    @DeleteMapping
    @Operation(summary = "Delete user",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "User deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (appears when spring security take care)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorSingleResponse.class)))
        }
    )
    public ResponseEntity<HttpStatus> deleteUser(@RequestHeader("Authorization") String authorization)
        throws UserNotFoundException {
        
        userServiceKafkaImp.deleteUserById(jwtUtil.extractUserId(authorization));
        
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
    
}
