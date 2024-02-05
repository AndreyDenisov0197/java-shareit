package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemStorage {
    private final Map<Integer, Item> allItems = new HashMap<>();
    private final Map<Integer, Map<Integer, Item>> allItemByUser = new HashMap<>();
    private int index = 1;
    private final UserStorage userStorage;


    public Item createItem(int userId, Item item) {
        item.setId(index++);
        item.setOwner(userId);

        int id = item.getId();
        allItems.put(id, item);

        Map<Integer, Item> itemsUser;
        if (allItemByUser.containsKey(userId)) {
            itemsUser = allItemByUser.get(userId);
        } else {
            itemsUser = new HashMap<>();
        }
        itemsUser.put(id, item);
        allItemByUser.put(userId, itemsUser);

        log.info("Элемент {} создан", item.getName());
        return allItems.get(id);
    }

    public Item updateItem(int userId, Item item, int itemId) {
        if (!allItems.containsKey(itemId)) {
            throw new ObjectNotFoundException("Такого элемента не существует!");
        }

        Item itemFromMap = allItems.get(itemId);
        if (userId != itemFromMap.getOwner()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не является владельцем элемента!");
        }

        String name = item.getName();
        if (name != null && !itemFromMap.getName().equals(name)) {
            itemFromMap.setName(name);
        }

        String description = item.getDescription();
        if (description != null && !itemFromMap.getDescription().equals(description)) {
            itemFromMap.setDescription(description);
        }

        Boolean available = item.getAvailable();
        if (available != itemFromMap.getAvailable() && available != null) {
            itemFromMap.setAvailable(available);
        }

        allItems.put(itemId, itemFromMap);
        Map<Integer, Item> itemsUser = allItemByUser.get(userId);
        allItemByUser.put(itemId, itemsUser);

        return allItems.get(itemId);
    }

    public ItemDto getItemById(int id) {
        return ItemMapper.toItemDto(allItems.get(id));
    }

    public List<Item> getItemsByUser(int userId) {
        if (!allItemByUser.containsKey(userId)) {
            throw new ObjectNotFoundException("У пользователя нет элементов!");
        }

        return new ArrayList<>(allItemByUser.get(userId).values());
    }


    public List<ItemDto> searchItem(String text) {
        List<ItemDto> itemList = new ArrayList<>();
        if (text.isBlank()) {
            return itemList;
        }

        for (Item i : allItems.values()) {
            if ((i.getName().toLowerCase().contains(text.toLowerCase())
                    || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                    && i.getAvailable().equals(true)) {
                itemList.add(ItemMapper.toItemDto(i));
            }
        }
        return itemList;
    }


}
