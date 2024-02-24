package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class LastNextBookingMapper {
    public static LastNextBookingDto mapBookingToLastNextDto(Booking booking) {
        return new LastNextBookingDto(
                booking.getId(),
                booking.getBooker().getId());
    }
}
