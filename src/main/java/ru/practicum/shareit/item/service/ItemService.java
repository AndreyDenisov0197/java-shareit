package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(int userId, ItemDto itemDto);

    ItemDto updateItem(int userId, ItemDto itemDto, int itemId);

    ItemDto getItemById(int itemId);

    List<ItemDto> getItemsByUser(int userId);

    List<ItemDto> searchItem(String text);
}
