package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    private User itemOwner;
    private Request request;

    @BeforeEach
    void setup() {
        this.itemOwner = addUser("Jason", "jason@ya.ru");
        User userRequesting = addUser("Shaun", "shaun@ya.ru");
       // this.request = addRequest("Looking for a balalaika.", LocalDateTime.now().minusDays(6), userRequesting);
        addItem("Balalaika", "Brand new balalaika", true, itemOwner, request);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void findByOwnerId() {
        List<Item> itemsSaved = itemRepository.findByOwnerId(itemOwner.getId());

        assertNotNull(itemsSaved);
        assertEquals(1, itemsSaved.size());

        Item itemSaved = itemsSaved.get(0);

        assertNotNull(itemSaved);
        assertEquals(itemOwner.getId(), itemSaved.getOwner().getId());
    }

    @Test
    void findByRequestId() {
        List<Item> itemsSaved = itemRepository.findByRequestId(request.getId());

        assertNotNull(itemsSaved);
        assertEquals(1, itemsSaved.size());

        Item itemSaved = itemsSaved.get(0);

        assertNotNull(itemSaved);
        assertEquals(request.getId(), itemSaved.getRequest().getId());
    }

    private Item addItem(String name, String description, boolean available, User owner, Request request) {
        Item itemToSave = new Item();
        itemToSave.setName(name);
        itemToSave.setDescription(description);
        itemToSave.setAvailable(available);
        itemToSave.setOwner(owner);
        itemToSave.setRequest(request);
        return itemRepository.save(itemToSave);
    }

    private User addUser(String name, String email) {
        User userToSave = new User();
        userToSave.setName(name);
        userToSave.setEmail(email);
        return userRepository.save(userToSave);
    }

/*    private Request addRequest(String description, LocalDateTime created, User userRequesting) {
        Request requestToSave = new Request();
        requestToSave.setDescription(description);
        requestToSave.setCreated(created);
        requestToSave.setRequestor(userRequesting);
        return requestRepository.save(requestToSave);
    }*/
}

















/*@DataJpaTest
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

    @BeforeEach
    public void beforeEach() {
        User userToSave = User.builder()
                .name("name")
                .email("email@email.com")
                .build();
        user = userRepository.save(userToSave);

        *//*User userToSave2 = User.builder()
                .name("nameUser")
                .email("email2@email.com")
                .build();
        user2 = userRepository.save(userToSave2);

        Request requestToSave = Request.builder()
                .description("qwerty")
                .created(LocalDateTime.now())
                .requestor(user2)
                .build();
        request = requestRepository.save(requestToSave);*//*

        Item itemToSave = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        item = itemRepository.save(itemToSave);
    }

    @AfterEach
    public void deleteItems() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void findByOwnerId() {
        *//*Optional<User> user1 = userRepository.findById(user.getId());
        assertTrue(user1.isPresent());
        assertEquals(user.getId(), user1.get().getId());*//*
        List<Item> items = itemRepository.findByOwnerId(user.getId());

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "name");
    }

    @Test
    void testFindByOwnerId() {
        *//*List<Item> items = itemRepository.findByOwnerId(1L, PageRequest.of(0, 10));

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "name");*//*
    }

    @Test
    void search() {*//*
        List<Item> items = itemRepository.search("cRIpt", PageRequest.of(0, 10));

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getDescription(), "description");*//*
    }

    @Test
    void findByRequestId() {*//*
        List<Item> items = itemRepository.findByRequestId(1L);

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getRequest().getDescription(), "qwerty");*//*
    }
    }*/