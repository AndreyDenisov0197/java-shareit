package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceIml implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(int userId, ItemDto itemDto) {
        userStorage.getUserById(userId);
        return itemStorage.createItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(int userId, ItemDto itemDto, int itemId) {
        return itemStorage.updateItem(userId, itemDto, itemId);
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsByUser(int userId) {
        return itemStorage.getItemsByUser(userId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text);
    }
}
