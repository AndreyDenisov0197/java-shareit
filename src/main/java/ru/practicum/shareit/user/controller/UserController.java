package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable @Min(0) Long id) {
        log.info("get /{}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> findAllUsers() {
        log.info("get");
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        log.info("post {}", user);
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable @Min(0) Long id,
                                          @Validated(ValidationMarker.OnUpdate.class)
                                                      @RequestBody UserDto userDto) {
        log.info("patch id-{}, user {}", id, userDto);
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @Min(0) Long id) {
        log.info("delete {}", id);
        userService.deleteUser(id);
    }

}
