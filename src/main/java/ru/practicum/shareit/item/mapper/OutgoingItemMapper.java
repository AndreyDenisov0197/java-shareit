package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.model.Item;


public class OutgoingItemMapper {
    public static OutgoingItemDto toOutgoingItemCollection(Item item) {
        return new OutgoingItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                null
        );
    }
}
