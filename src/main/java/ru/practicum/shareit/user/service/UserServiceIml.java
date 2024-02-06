package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto getUserById(int id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return userStorage.addUser(userDto);
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        return userStorage.updateUser(id, userDto);
    }

    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }
}
