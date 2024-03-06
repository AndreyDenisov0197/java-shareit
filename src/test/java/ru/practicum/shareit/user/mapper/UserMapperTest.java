package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("nameUser")
                .email("email@mail.ru")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("nameUser")
                .email("email@mail.ru")
                .build();
    }

    @Test
    void toUserDto() {
        UserDto result = UserMapper.toUserDto(user);

        assertEquals(userDto, result);
    }

    @Test
    void toUser() {
        User result = UserMapper.toUser(userDto);

        assertEquals(user, result);
    }
}