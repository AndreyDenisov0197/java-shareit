package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long id;

    @NotBlank(groups = ValidationMarker.OnCreate.class, message = "Request description field is blank!")
    private String description;

    private Long requestor;

    private LocalDateTime created;
}
