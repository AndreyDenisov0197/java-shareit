package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {

    private final UserStorage userStorage;

    @Override
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(int id, User user) {
        return userStorage.updateUser(id, user);
    }

    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }
}
