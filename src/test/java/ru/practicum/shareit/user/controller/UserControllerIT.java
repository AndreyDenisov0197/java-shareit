package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerIT {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserServiceImpl userService;

    @SneakyThrows
    @Test
    void getById() {
        long id = 1L;
        mvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk());

        verify(userService).getUserById(id);
    }

    @SneakyThrows
    @Test
    void getById_whenUserIdNotPositive_thenReturnBadRequest() {
        long id = -1L;
        mvc.perform(get("/users/{id}", id))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(id);
    }

    @SneakyThrows
    @Test
    void findAllUsers() {
        mvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getUsers();
    }

    @SneakyThrows
    @Test
    void createUser() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("name");
        userToCreate.setEmail("email@mail.ru");
        when(userService.addUser(userToCreate)).thenReturn(userToCreate);

        String result = mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(userToCreate), result);
    }


    @SneakyThrows
    @Test
    void createUser_whenUserNotName_thenReturnValidationException() {
        UserDto userToCreate = new UserDto();
        userToCreate.setEmail("email@mail.ru");
        when(userService.addUser(userToCreate)).thenReturn(userToCreate);

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(userToCreate);
    }

    @SneakyThrows
    @Test
    void update() {
        long id = 0L;
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@mail.ru");

        UserDto newUserDto = new UserDto(0L, "name", "email@mail.ru");
        when(userService.updateUser(id, userDto)).thenReturn(newUserDto);

        String result = mvc.perform(patch("/users/{id}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(newUserDto), result);
    }

    @SneakyThrows
    @Test
    void update_whenUserNotEmail_thenReturnValidationException() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail("emailmail.ru");
        long id = 0L;
        when(userService.updateUser(id, user)).thenReturn(user);

        mvc.perform(patch("/users/{id}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(user);
    }

    @SneakyThrows
    @Test
    void update_whenUserIdNotPositive_thenReturnValidationException() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail("email@mail.ru");
        long id = -1L;
        when(userService.updateUser(id, user)).thenReturn(user);

        mvc.perform(patch("/users/{id}", id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(user);
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        long id = 1L;
        mvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        verify(userService).deleteUser(id);
    }

    @SneakyThrows
    @Test
    void deleteUser_whenUserIdNotPositive_thenReturnValidationException() {
        long id = -1L;
        mvc.perform(delete("/users/{id}", id))
                .andExpect(status().isBadRequest());

        verify(userService, never()).deleteUser(id);
    }
}