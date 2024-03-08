package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
        userDto = new UserDto(1L, "nameUser", "qwerty");
    }

    @Test
    void getById_whenIncoked_thenResponseStatusOkWhithUsersCollectionInBody() {
        Mockito.when(userService.getUserById(userDto.getId())).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getById(userDto.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void findAllUsers_whenIncoked_thenResponseStatusOkWhithUsersCollectionInBody() {
        Collection<UserDto> expectedUsers = List.of(userDto);
        Mockito.when(userService.getUsers()).thenReturn(expectedUsers);

        ResponseEntity<Collection<UserDto>> response = userController.findAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
    }

    @Test
    void createUser() {
        Mockito.when(userService.addUser(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertEquals(userDto, response.getBody());
        verify(userService).addUser(userDto);
    }

    @Test
    void update() {
        Mockito.when(userService.updateUser(userDto.getId(), userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.update(userDto.getId(), userDto);

        assertEquals(userDto, response.getBody());
        verify(userService).updateUser(userDto.getId(), userDto);
    }

    @Test
    void deleteUser() {
        long id = 0L;

        userController.deleteUser(id);

        verify(userService).deleteUser(id);
    }
}