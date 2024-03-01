package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.ValidationMarker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable @Positive Long id) {
        log.info("get /{}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<UserDto> findAllUsers() {
        log.info("get");
        return userService.getUsers();
    }

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        log.info("post {}", user);
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    @Validated(ValidationMarker.OnUpdate.class)
    public UserDto update(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        log.info("patch id-{}, user {}", id, userDto);
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("delete {}", id);
        userService.deleteUser(id);
    }

}
