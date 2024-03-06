package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;
    private BookingRestDto bookingRestDto;
    private BookingDto bookingDto;

    @BeforeEach
    public void beforeEach() {
        bookingRestDto = BookingRestDto.builder()
                .itemId(2L)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(4))
                .build();

        bookingDto = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(4))
                .booker(new User())
                .item(new ItemDto())
                .status(BookingStatus.CANCELED)
                .build();
    }

    @Test
    void bookingRequest() {
        long userId = 3L;
        when(bookingService.bookingRequest(bookingRestDto, userId)).thenReturn(bookingDto);

        ResponseEntity<BookingDto> response = bookingController.bookingRequest(bookingRestDto, userId);

        assertEquals(bookingDto, response.getBody());
        verify(bookingService).bookingRequest(bookingRestDto, userId);
    }

    @Test
    void bookingConfirmation() {
        long bookingId = 2L;
        long userId = 3L;
        boolean approved = true;
        when(bookingService.bookingConfirmation(bookingId, userId, approved)).thenReturn(bookingDto);

        ResponseEntity<BookingDto> response = bookingController.bookingConfirmation(bookingId, userId, approved);

        assertEquals(bookingDto, response.getBody());
        verify(bookingService).bookingConfirmation(bookingId, userId, approved);
    }

    @Test
    void getBooking() {
        long bookingId = 2L;
        long userId = 3L;
        when(bookingService.getBooking(bookingId, userId)).thenReturn(bookingDto);

        ResponseEntity<BookingDto> response = bookingController.getBooking(bookingId, userId);

        assertEquals(bookingDto, response.getBody());
        verify(bookingService).getBooking(bookingId, userId);
    }

    @Test
    void getAllBookingForBooker() {
        BookingState state = BookingState.ALL;
        long userId = 2L;
        int from = 0;
        int size = 10;
        when(bookingService.getAllBookingForBooker(state, userId, from, size)).thenReturn(List.of(bookingDto));

        ResponseEntity<Collection<BookingDto>> response =
                bookingController.getAllBookingForBooker(state.toString(), userId, from, size);

        assertEquals(List.of(bookingDto), response.getBody());
        verify(bookingService).getAllBookingForBooker(state, userId, from, size);
    }

    @Test
    void getAllBookingForUserItems() {
        BookingState state = BookingState.ALL;
        long userId = 2L;
        int from = 0;
        int size = 10;
        when(bookingService.getAllBookingForUserItems(state, userId, from, size)).thenReturn(List.of(bookingDto));

        ResponseEntity<Collection<BookingDto>> response =
                bookingController.getAllBookingForUserItems(state.toString(), userId, from, size);

        assertEquals(List.of(bookingDto), response.getBody());
        verify(bookingService).getAllBookingForUserItems(state, userId, from, size);
    }
}