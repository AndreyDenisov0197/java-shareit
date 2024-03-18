package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    OutgoingItemDto getItemById(Long itemId, Long userId);

    Collection<ItemDto> searchItem(String text, int from, int size);

    ItemCommentsDto createComments(Long itemId, Long userId, CommentDto comment);

    Collection<OutgoingItemDto> getItemsByUser(Long userId, int from, int size);
}