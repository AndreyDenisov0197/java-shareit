package ru.practicum.shareit.user.mapper;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;

@Validated
public class UserMapper {

    public static UserDto toUserDto(@Valid User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(@Valid UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
