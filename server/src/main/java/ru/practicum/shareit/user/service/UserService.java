package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto getUserById(long id);

    Collection<UserDto> getUsers();

    UserDto addUser(UserDto user);

    UserDto updateUser(long id, UserDto userDto);

    void deleteUser(long id);
}