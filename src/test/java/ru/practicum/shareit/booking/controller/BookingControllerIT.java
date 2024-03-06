package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceDb;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.controller.BookingController.HEADER;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerIT {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingServiceDb service;
    private final String json = "application/json";

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


    @SneakyThrows
    @Test
    void bookingRequest() {
        long booker = 3L;
        when(service.bookingRequest(bookingRestDto, booker)).thenReturn(bookingDto);

        String result = mvc.perform(post("/bookings")
                        .contentType(json)
                        .header(HEADER, booker)
                        .content(mapper.writeValueAsString(bookingRestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void bookingRequest_whenBookingNotStart() {
        long booker = 3L;
        bookingRestDto.setStart(null);
        when(service.bookingRequest(bookingRestDto, booker)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(json)
                        .header(HEADER, booker)
                        .content(mapper.writeValueAsString(bookingRestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void bookingRequest_whenBookingNotEnd() {
        long booker = 3L;
        bookingRestDto.setEnd(null);
        when(service.bookingRequest(bookingRestDto, booker)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(json)
                        .header(HEADER, booker)
                        .content(mapper.writeValueAsString(bookingRestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void bookingRequest_whenBookingStartBefore() {
        long booker = 3L;
        bookingRestDto.setStart(LocalDateTime.now().minusDays(2));
        when(service.bookingRequest(bookingRestDto, booker)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(json)
                        .header(HEADER, booker)
                        .content(mapper.writeValueAsString(bookingRestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void bookingRequest_whenBookingEndBefore() {
        long booker = 3L;
        bookingRestDto.setEnd(LocalDateTime.now().minusDays(2));
        when(service.bookingRequest(bookingRestDto, booker)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(json)
                        .header(HEADER, booker)
                        .content(mapper.writeValueAsString(bookingRestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void bookingRequest_whenBookingStartAfterEnd() {
        long booker = 3L;
        bookingRestDto.setEnd(LocalDateTime.now().plusDays(1));
        when(service.bookingRequest(bookingRestDto, booker)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(json)
                        .header(HEADER, booker)
                        .content(mapper.writeValueAsString(bookingRestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void bookingRequest_whenBookingStartEqualsEnd() {
        long booker = 3L;
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        bookingRestDto.setStart(date);
        bookingRestDto.setEnd(date);
        when(service.bookingRequest(bookingRestDto, booker)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(json)
                        .header(HEADER, booker)
                        .content(mapper.writeValueAsString(bookingRestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void bookingConfirmation() {
        long bookingId = 2L;
        long userId = 3L;
        boolean approved = true;
        when(service.bookingConfirmation(bookingId, userId, approved)).thenReturn(bookingDto);

        String result = mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", String.valueOf(approved))
                        .contentType(json)
                        .header(HEADER, userId)
                        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getBooking() {
        long bookingId = 2L;
        long userId = 3L;
        when(service.getBooking(bookingId, userId)).thenReturn(bookingDto);

        String result = mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType(json)
                        .header(HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingForBooker() {
        BookingState state = BookingState.ALL;
        long userId = 2L;
        int from = 0;
        int size = 10;

        when(service.getAllBookingForBooker(state, userId, from, size)).thenReturn(List.of(bookingDto));

        String result = mvc.perform(get("/bookings")
                        .contentType(json)
                        .header(HEADER, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingForBooker_whenStateNotFound() {
        String state = "ALLS";
        long userId = 2L;
        int from = 0;
        int size = 10;

        mvc.perform(get("/bookings")
                        .contentType(json)
                        .header(HEADER, userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookingForUserItems() {
        BookingState state = BookingState.ALL;
        long userId = 2L;
        int from = 0;
        int size = 10;

        when(service.getAllBookingForUserItems(state, userId, from, size)).thenReturn(List.of(bookingDto));

        String result = mvc.perform(get("/bookings/owner")
                        .contentType(json)
                        .header(HEADER, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingForUserItems_whenStateNotFound() {
        String state = "   ";
        long userId = 2L;
        int from = 0;
        int size = 10;

        mvc.perform(get("/bookings/owner")
                        .contentType(json)
                        .header(HEADER, userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest());
    }
}