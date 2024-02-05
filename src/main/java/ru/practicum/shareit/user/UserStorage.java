package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserStorage {
    private final Map<Integer, User> allUsers = new HashMap<>();
    private int index = 1;

    public User addUser(User user) {
        validateName(user.getName());
        validateEmail(user.getEmail());
        user.setId(index++);

        int id = user.getId();
        allUsers.put(id, user);
        log.info("Создан новый пользователь с ID={}", id);
        return allUsers.get(id);
    }

    public void deleteUser(int id) {
        if (!allUsers.containsKey(id)) {
            throw new ObjectNotFoundException("Такого пользователя не существует!");
        }

        allUsers.remove(id);
        log.info("Пользователь с ID={} удален", id);
    }

    public User updateUser(int id, User user) {
        if (!allUsers.containsKey(id)) {
            throw new ObjectNotFoundException("Такого пользователя не существует!");
        }
        User userUpdate = allUsers.get(id);
        String userName = user.getName();
        if (userName != null && !userUpdate.getName().equals(userName)) {
            userUpdate.setName(userName);
        }

        String userEmail = user.getEmail();
        if (userEmail != null && !userUpdate.getEmail().equals(userEmail)) {
            validateEmail(userEmail);
            userUpdate.setEmail(userEmail);
        }

        allUsers.put(id, userUpdate);
        log.info("У пользователя с ID={} обновлено имя", id);
        return allUsers.get(id);
    }


    public User getUserById(int id) {
        if (!allUsers.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого пользователя не существует!");
        }

        User user = allUsers.get(id);
        log.info("Пользователь с ID={} получен", id);
        return user;
    }

    public List<User> getUsers() {
        if (allUsers.isEmpty()) {
            throw new ObjectNotFoundException("Список пользователей пуст");
        }

        List<User> users = new ArrayList<>(allUsers.values());
        log.info("Получили всех пользователей!");
        return users;
    }

    private void validateName(String name) {
        if (name == null) {
            log.error("Пустое имя пользователя");
            throw new ValidateException("Пустое имя пользователя");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            log.error("Неправильно введен Email");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно введен Email");
        }

        for (User us : allUsers.values()) {
            if (email.equals(us.getEmail())) {
                log.error("Пользователь с Email = {} уже существует!", email);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с Email уже существует!");
            }
        }
    }
}