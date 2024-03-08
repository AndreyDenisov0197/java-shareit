package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    private User user;
    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "nameUser", "email@mail.ru");
        userDto = UserMapper.toUserDto(user);
    }

    @Test
    void getUserById_whenUserFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(user.getId());

        assertEquals(userDto, result);
    }

    @Test
    void getUserById_whenUserNotFound() {
        long id = 0L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(id));
    }

    @Test
    void getUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        Collection<UserDto> expectedUsersDto = List.of(userDto);

        Collection<UserDto> result = userService.getUsers();

        assertEquals(expectedUsersDto, result);
    }

    @Test
    void addUser() {
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.addUser(userDto);

        assertEquals(userDto, result);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser() {
        UserDto updateUserDto = new UserDto();
        updateUserDto.setName("name2");
        updateUserDto.setEmail("email2@mail.ru");
        User expectedUser = new User(1L, "name2", "email2@mail.ru");
        UserDto expectedUserDto = UserMapper.toUserDto(expectedUser);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto result = userService.updateUser(user.getId(), updateUserDto);

        assertEquals(expectedUserDto, result);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(expectedUser, userArgumentCaptor.getValue());
    }

    @Test
    void updateUser_whenUserUpdateName() {
        UserDto updateUserDto = new UserDto();
        updateUserDto.setName("name");
        User expectedUser = new User(1L, "name", "email@mail.ru");
        UserDto expectedUserDto = UserMapper.toUserDto(expectedUser);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto result = userService.updateUser(user.getId(), updateUserDto);

        assertEquals(expectedUserDto, result);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(expectedUser, userArgumentCaptor.getValue());
    }

    @Test
    void updateUser_whenUserUpdateEmail() {
        UserDto updateUserDto = new UserDto();
        updateUserDto.setEmail("email2@mail.ru");
        User expectedUser = new User(1L, "nameUser", "email2@mail.ru");
        UserDto expectedUserDto = UserMapper.toUserDto(expectedUser);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto result = userService.updateUser(user.getId(), updateUserDto);

        assertEquals(expectedUserDto, result);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(expectedUser, userArgumentCaptor.getValue());
    }

    @Test
    void updateUser_whenUserNotFound() {
        long id = 0L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.updateUser(id, userDto));
    }

    @Test
    void deleteUser() {
        long id = 0L;

        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }
}