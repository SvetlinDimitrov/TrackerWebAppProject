package org.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "The User API handles user registration, login, details retrieval, profile editing, and account deletion.")
public interface UserController {

    @Operation(summary = "Create a new user account", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class))),
            @ApiResponse(responseCode = "400", description = "Wrong User Credentials",
                    content = @Content(schema = @Schema(implementation = WrongUserCredentialsException.class)))})
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> createUserAccount(
            @Valid @RequestBody RegisterUserDto registerUserDto,
            BindingResult result) throws WrongUserCredentialsException;

    @Operation(summary = "Login a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = JwtTokenView.class))),
            @ApiResponse(responseCode = "400", description = "Wrong User Credentials",
                    content = @Content(schema = @Schema(implementation = WrongUserCredentialsException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @PostMapping("/login")
    public ResponseEntity<JwtTokenView> login(
            @RequestBody LoginUserDto userDto) throws WrongUserCredentialsException;

    @Operation(summary = "Get user details", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserView.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @GetMapping(path = "/details")
    public ResponseEntity<UserView> getUserDetails(
            @Parameter(description = "Authorization token")
            @RequestHeader("Authorization") String authorization);

    @Operation(summary = "Edit user profile", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = JwtTokenView.class))),
            @ApiResponse(responseCode = "400", description = "User Not Found",
                    content = @Content(schema = @Schema(implementation = UserNotFoundException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @PatchMapping("/edit")
    public ResponseEntity<JwtTokenView> editUserProfile(
            @RequestBody EditUserDto dto,
            @Parameter(description = "Authorization token")
            @RequestHeader("Authorization") String authorization) throws UserNotFoundException;

    @Operation(summary = "Delete a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "User Not Found",
                    content = @Content(schema = @Schema(implementation = UserNotFoundException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(
            @Parameter(description = "Authorization token")
            @RequestHeader("Authorization") String authorization)
            throws UserNotFoundException;
}