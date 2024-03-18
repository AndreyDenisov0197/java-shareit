package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                booking.getBooker(),
                booking.getStatus()
        );
    }

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

    public static LastNextBookingDto mapBookingToLastNextDto(Booking booking) {
        return new LastNextBookingDto(
                booking.getId(),
                booking.getBooker().getId());
    }

}