package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Item name field is blank!")
    @Size(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class}, max = 128,
            message = "Creating or updating item name field is bigger than 128 characters!")
    private String name;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Item description field is blank!")
    private String description;

    @NotNull(groups = {ValidationMarker.OnCreate.class})
    private Boolean available;

    private Long requestId;
}
