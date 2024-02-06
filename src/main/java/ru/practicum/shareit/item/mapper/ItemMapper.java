package ru.practicum.shareit.item.mapper;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import javax.validation.Valid;

@Validated
public class ItemMapper {
    private static final ItemRequestStorage requestStorage = new ItemRequestStorage();

    public static ItemDto toItemDto(@Valid Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(@Valid ItemDto itemDto, int owner) {
        return new Item(
                itemDto.getId(),
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequest() != null ? requestStorage.getRequestById(itemDto.getRequest()) : null
        );
    }

}
