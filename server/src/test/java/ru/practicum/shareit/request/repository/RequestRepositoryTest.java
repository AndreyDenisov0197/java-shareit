package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

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
    private User user2;
    private Request request;
    private Request request2;
    private Request request3;


    private Sort sort;

    @BeforeEach
    void beforeEach() {
        User userToSave = User.builder().name("name").email("email@mail.ru").build();
        user = userRepository.save(userToSave);

        User userToSave2 = User.builder().name("name2").email("email2@mail.ru").build();
        user2 = userRepository.save(userToSave2);

        Request requestToSave = Request.builder().description("text").requestor(user)
                .created(LocalDateTime.now()).build();
        request = requestRepository.save(requestToSave);


        Request requestToSave2 = Request.builder().description("text2").requestor(user2)
                .created(LocalDateTime.now()).build();
        request2 = requestRepository.save(requestToSave2);

        Request requestToSave3 = Request.builder().description("text3").requestor(user2)
                .created(LocalDateTime.now()).build();
        request3 = requestRepository.save(requestToSave3);
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
        assertEquals(List.of(request3, request2), requestRepository
                .findByRequestor_IdNotOrderByCreatedDesc(user.getId(), PageRequest.of(0,10)));
    }

    @Test
    void findByRequestorIdNotLike_whenPageRequestFrom1Size1() {
        assertEquals(List.of(request2), requestRepository
                .findByRequestor_IdNotOrderByCreatedDesc(user.getId(), PageRequest.of(1,1)));
    }

}