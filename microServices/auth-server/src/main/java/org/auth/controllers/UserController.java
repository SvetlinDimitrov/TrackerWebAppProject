package org.auth.controllers;

import jakarta.validation.Valid;
import org.auth.exceptions.UserNotFoundException;
import org.auth.exceptions.WrongUserCredentialsException;
import org.auth.model.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public interface UserController {

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> createUserAccount(@Valid @RequestBody RegisterUserDto registerUserDto,
                                                        BindingResult result) throws WrongUserCredentialsException;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenView> login(@RequestBody LoginUserDto userDto) throws WrongUserCredentialsException;

    @GetMapping(path = "/details")
    public ResponseEntity<UserView> getUserDetails(@RequestHeader("Authorization") String authorization);

    @PatchMapping("/edit")
    public ResponseEntity<JwtTokenView> editUserProfile(
            @RequestBody EditUserDto dto,
            @RequestHeader("Authorization") String authorization) throws UserNotFoundException;

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(@RequestHeader("Authorization") String authorization)
            throws UserNotFoundException;
}
