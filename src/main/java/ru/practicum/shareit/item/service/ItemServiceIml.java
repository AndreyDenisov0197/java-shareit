package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceIml implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Item createItem(int userId, Item item) {
        validate(item, userId);
        return itemStorage.createItem(userId, item);
    }

    @Override
    public Item updateItem(int userId, Item item, int itemId) {
        return itemStorage.updateItem(userId, item, itemId);
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public List<Item> getItemsByUser(int userId) {
        return itemStorage.getItemsByUser(userId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text);
    }

    private void validate(Item item, int userId) {
        String name = item.getName();
        if (name == null || name.isBlank()) {
            log.error("Пустое  название");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пустое название");
        }

        String description = item.getDescription();
        if (description == null || description.isBlank()) {
            log.error("Пустое описание");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пустое описание");
        }
        userStorage.getUserById(userId);

        Boolean available = item.getAvailable();
        if (available == null) {
            log.error("Нет в наличии");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нет в наличии");
        }
    }
}
