package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    private User user;
    private User user2;
    private Request request;
    private Item item;
    private Item item2;


    @BeforeEach
    public void beforeEach() {
        User userToSave = User.builder().name("name").email("email@email.com").build();
        user = userRepository.save(userToSave);

        User userToSave2 = User.builder().name("nameUser").email("email2@email.com").build();
        user2 = userRepository.save(userToSave2);

        Request requestToSave = Request.builder().description("qwerty").created(LocalDateTime.now())
                .requestor(user2).build();
        request = requestRepository.save(requestToSave);

        Item itemToSave = Item.builder().name("name").description("description")
                .available(true).owner(user).request(request).build();
        item = itemRepository.save(itemToSave);

        Item itemToSave2 = Item.builder().name("script").description("text")
                .available(true).owner(user).request(request).build();
        item2 = itemRepository.save(itemToSave2);
    }

    @AfterEach
    public void deleteItems() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void findByOwnerId() {
        List<Item> items = itemRepository.findByOwnerId(user.getId());

        assertEquals(List.of(item, item2), items);
        assertEquals(2, items.size());
        assertEquals("name", items.get(0).getName());
    }

    @Test
    void findByOwnerId_whenPageRequestFrom1Size1() {
        List<Item> items = itemRepository.findByOwnerId(user.getId(), PageRequest.of(1,1));

        assertEquals(List.of(item2), items);
        assertEquals(1, items.size());
        assertEquals("script", items.get(0).getName());
    }

    @Test
    void search() {
        List<Item> items = itemRepository.search("cRIpt", PageRequest.of(0, 10));

        assertEquals(List.of(item, item2), items);
        assertEquals(2, items.size());
        assertEquals("description", items.get(0).getDescription());
        assertEquals("script", items.get(1).getName());
    }

    @Test
    void search_whenPageRequestFrom1Size1() {
        List<Item> items = itemRepository.search("cRIpt", PageRequest.of(1, 1));

        assertEquals(List.of(item2), items);
        assertEquals(1, items.size());
        assertEquals("script", items.get(0).getName());
    }

    @Test
    void findByRequestId() {
        List<Item> items = itemRepository.findByRequestId(request.getId());

        assertEquals(List.of(item, item2), items);
        assertEquals(2, items.size());
        assertEquals("qwerty", items.get(0).getRequest().getDescription());
    }
}