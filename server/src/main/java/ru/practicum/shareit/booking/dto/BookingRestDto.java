package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class BookingRestDto {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}