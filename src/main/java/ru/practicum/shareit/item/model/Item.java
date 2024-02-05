package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private int id;
    private int owner;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
}