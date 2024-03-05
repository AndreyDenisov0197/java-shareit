package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private Request request;
    private Sort sort;

    @BeforeEach
    void beforeEach() {
        User userToSave = User.builder()
                .name("name")
                .email("email@mail.ru")
                .build();
        user = userRepository.save(userToSave);

        Request requestToSave = Request.builder()
                .description("text")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        request = requestRepository.save(requestToSave);
        sort = Sort.by(Sort.Direction.DESC, "created");
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void findByRequestorId() {
        assertEquals(List.of(request), requestRepository.findByRequestorId(user.getId(), sort));
    }

    @Test
    void findByRequestorIdNotLike() {
    }
}