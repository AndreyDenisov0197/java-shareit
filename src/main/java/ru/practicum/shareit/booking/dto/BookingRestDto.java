package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validation.BookingDateTimeValidation;
import ru.practicum.shareit.validation.ValidationMarker;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@BookingDateTimeValidation(groups = ValidationMarker.OnCreate.class)
public class BookingRestDto {

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
