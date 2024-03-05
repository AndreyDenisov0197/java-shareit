package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private User user2;
    private Item item;
    private Comment comment;

    @BeforeEach
    void beforeEach() {
        User userToSave = User.builder()
                .name("nameUser")
                .email("email@mail.ru")
                .build();
        user = userRepository.save(userToSave);

        User userToSave2 = User.builder()
                .name("nameUser2")
                .email("email2@mail.ru")
                .build();
        user2 = userRepository.save(userToSave);

        Item itemToSave = Item.builder()
                .name("nameItem")
                .description("Qwerty")
                .owner(user)
                .available(true)
                .build();
        item = itemRepository.save(itemToSave);

        Comment commentToSave = Comment.builder()
                .text("text")
                .author(user2)
                .item(item)
                .created(LocalDateTime.now())
                .build();
        comment = commentRepository.save(commentToSave);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findByItemId() {
        assertEquals(List.of(comment), commentRepository.findByItemId(item.getId()));
    }

    @Test
    void findByItemId_whenItemIdNotFound() {
        assertEquals(List.of(), commentRepository.findByItemId(35L));

    }
}