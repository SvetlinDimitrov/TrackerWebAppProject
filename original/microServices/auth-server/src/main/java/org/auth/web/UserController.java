package org.auth.web;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.auth.features.user.dto.UserCreateRequest;
import org.auth.features.user.services.UserService;
import org.auth.infrastructure.rabbitmq.UserRabbitmqService;
import org.example.domain.user.dto.UserEditRequest;
import org.example.domain.user.dto.UserView;
import org.example.domain.user.paths.UserControllerPaths;
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
@RequestMapping(UserControllerPaths.BASE)
@RequiredArgsConstructor
public class UserController {

  private final UserService service;
  private final UserRabbitmqService userRabbitmqService;

  @PostMapping(UserControllerPaths.CREATE)
  public ResponseEntity<UserView> create(
      @RequestBody @Valid UserCreateRequest registerUserDto) {
    UserView createdUser = userRabbitmqService.create(registerUserDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @GetMapping(UserControllerPaths.ME)
  public ResponseEntity<UserView> me() {
    return ResponseEntity.status(HttpStatus.OK).body(service.me());
  }

  @GetMapping(UserControllerPaths.GET_BY_ID)
  public ResponseEntity<UserView> get(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(service.getById(id));
  }

  @PatchMapping(UserControllerPaths.EDIT)
  public ResponseEntity<UserView> edit(@RequestBody @Valid UserEditRequest dto,
      @PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(service.edit(dto, id));
  }

  @DeleteMapping(UserControllerPaths.DELETE)
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    userRabbitmqService.delete(id);
    return ResponseEntity.noContent().build();
  }
}