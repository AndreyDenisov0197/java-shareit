package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingDto bookingRequest(BookingRestDto booking, Long booker);

    BookingDto bookingConfirmation(Long bookingId, Long userId, boolean status);

    BookingDto getBooking(Long bookingId, Long userId);

    Collection<BookingDto> getAllBookingForBooker(BookingState state, Long userId);

    Collection<BookingDto> getAllBookingForUserItems(BookingState state, Long userId);
}
