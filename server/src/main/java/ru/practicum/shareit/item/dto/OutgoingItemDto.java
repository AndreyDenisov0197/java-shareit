package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutgoingItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LastNextBookingDto lastBooking;
    private LastNextBookingDto nextBooking;
    private List<ItemCommentsDto> comments;

}