package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

public class BookingRestMapper {

    public static Booking toBooking(BookingRestDto booking) {
        return new Booking(
                null,
                booking.getStart(),
                booking.getEnd(),
                null,
                null,
                BookingStatus.WAITING
        );
    }
}
