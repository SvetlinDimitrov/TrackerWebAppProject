package org.auth.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.auth.features.user.dto.UserCreateRequest;
import org.example.domain.user.dto.UserEditRequest;
import org.auth.features.user.services.UserService;
import org.example.domain.user.dto.UserView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  @PostMapping
  public ResponseEntity<UserView> create(
      @RequestBody @Valid UserCreateRequest registerUserDto) {
    UserView createdUser = service.create(registerUserDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @GetMapping
  public ResponseEntity<UserView> me() {
    return ResponseEntity.status(HttpStatus.OK).body(service.me());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserView> get(@PathVariable String id) {
    return ResponseEntity.status(HttpStatus.OK).body(service.getById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserView> edit(@RequestBody @Valid UserEditRequest dto,
      @PathVariable String id) {
    return ResponseEntity.status(HttpStatus.OK).body(service.edit(dto, id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
