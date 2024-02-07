package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
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

    public UserDto addUser(UserDto userDto) {
        validateEmail(userDto.getEmail());
        User user = UserMapper.toUser(userDto);

        user.setId(index++);
        int id = user.getId();
        allUsers.put(id, user);
        log.info("Создан новый пользователь с ID={}", id);
        return UserMapper.toUserDto(allUsers.get(id));
    }

    public void deleteUser(int id) {
        if (!allUsers.containsKey(id)) {
            throw new ObjectNotFoundException("Такого пользователя не существует!");
        }

        allUsers.remove(id);
        log.info("Пользователь с ID={} удален", id);
    }

    public UserDto updateUser(int id, UserDto userDto) {
        if (!allUsers.containsKey(id)) {
            throw new ObjectNotFoundException("Такого пользователя не существует!");
        }
        User userUpdate = allUsers.get(id);
        String userName = userDto.getName();
        if (userName != null && !userUpdate.getName().equals(userName)) {
            userUpdate.setName(userName);
        }

        String userEmail = userDto.getEmail();
        if (userEmail != null && !userUpdate.getEmail().equals(userEmail)) {
            validateEmail(userEmail);
            userUpdate.setEmail(userEmail);
        }

        allUsers.put(id, userUpdate);
        log.info("У пользователя с ID={} обновлено имя", id);
        return UserMapper.toUserDto(allUsers.get(id));
    }


    public UserDto getUserById(int id) {
        if (!allUsers.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого пользователя не существует!");
        }

        UserDto userDto = UserMapper.toUserDto(allUsers.get(id));
        log.info("Пользователь с ID={} получен", id);
        return userDto;
    }

    public List<UserDto> getUsers() {
        if (allUsers.isEmpty()) {
            throw new ObjectNotFoundException("Список пользователей пуст");
        }

        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : allUsers.values()) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        log.info("Получили всех пользователей!");
        return userDtoList;
    }

    private void validateEmail(String email) {
        for (User us : allUsers.values()) {
            if (email.equals(us.getEmail())) {
                log.error("Пользователь с Email = {} уже существует!", email);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с Email уже существует!");
            }
        }
    }
}