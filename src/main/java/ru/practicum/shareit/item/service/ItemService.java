package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(int userId, Item item);

    Item updateItem(int userId, Item item, int itemId);

    ItemDto getItemById(int itemId);

    List<Item> getItemsByUser(int userId);

    List<ItemDto> searchItem(String text);
}
