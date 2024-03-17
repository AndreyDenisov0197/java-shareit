package ru.practicum.shareit.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;



    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable @Min(0) Long id) {
        log.info("get /{}", id);
        return userClient.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() throws JsonProcessingException {
        log.info("get");
        return userClient.getUsers();
    }

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        log.info("post {}", user);
        return userClient.postUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable @Min(0) Long id,
                                          @Validated(ValidationMarker.OnUpdate.class)
                                          @RequestBody UserDto userDto) {
        log.info("patch id-{}, user {}", id, userDto);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable @Min(0) Long id) {
        log.info("delete {}", id);
        return userClient.deleteUser(id);
    }

}

