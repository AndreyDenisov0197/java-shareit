package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void beforeEach() {
        User userToSave = User.builder()
                .name("name")
                .email("email@mail.ru")
                .build();
        user = userRepository.save(userToSave);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void checkUserById() {
        assertTrue(userRepository.existsById(user.getId()));
    }

    @Test
    void checkUserById_whenUserIdNotFound() {
        assertThrows(NotFoundException.class, () -> userRepository.checkUserById(35L));
    }
}