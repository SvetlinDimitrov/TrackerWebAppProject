package org.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.auth.model.dto.AuthRequestDto;
import org.auth.model.dto.JwtTokenView;
import org.auth.model.dto.UserCreateRequest;
import org.auth.model.dto.UserEditRequest;
import org.auth.model.dto.UserView;
import org.example.exceptions.throwable.BadRequestException;
import org.example.exceptions.throwable.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "The User API handles user registration, login, details retrieval, profile editing, and account deletion.")
public interface UserController {

    @Operation(summary = "Create a new user account", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = HttpStatus.class))),
            @ApiResponse(responseCode = "400", description = "Wrong User Credentials",
                content = @Content(schema = @Schema(implementation = BadRequestException.class)))})
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> create(
        @Valid @RequestBody UserCreateRequest registerUserDto);

    @Operation(summary = "Login a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = JwtTokenView.class))),
            @ApiResponse(responseCode = "400", description = "Wrong User Credentials",
                content = @Content(schema = @Schema(implementation = BadRequestException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @PostMapping("/login")
    public ResponseEntity<JwtTokenView> login(
        @RequestBody @Valid AuthRequestDto userDto);

    @Operation(summary = "Get user details", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserView.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @GetMapping(path = "/details")
    public ResponseEntity<UserView> me();

    @Operation(summary = "Edit user profile", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = JwtTokenView.class))),
            @ApiResponse(responseCode = "400", description = "User Not Found",
                content = @Content(schema = @Schema(implementation = NotFoundException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @PatchMapping("/edit")
    public ResponseEntity<JwtTokenView> edit(@RequestBody @Valid UserEditRequest dto);

    @Operation(summary = "Delete a user", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "User Not Found",
                content = @Content(schema = @Schema(implementation = NotFoundException.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = String.class)))})
    @DeleteMapping
    public ResponseEntity<HttpStatus> delete();
}