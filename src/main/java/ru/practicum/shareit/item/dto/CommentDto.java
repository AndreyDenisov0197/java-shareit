package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @Null(groups = ValidationMarker.OnCreate.class)
    private Long id;

    @NotBlank(groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
    @Size(groups = ValidationMarker.OnCreate.class, max = 512)
    private String text;
}
