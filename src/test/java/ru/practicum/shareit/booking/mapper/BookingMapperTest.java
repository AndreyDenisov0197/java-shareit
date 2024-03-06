package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {
    private Booking booking;
    private BookingRestDto bookingRestDto;
    private BookingDto bookingDto;
    @BeforeEach
    public void beforeEach() {
        LocalDateTime date = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .name("userName")
                .email("email@mail.ru")
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("user")
                .email("email2@mail.ru")
                .build();

        Item item = Item.builder()
                .id(2L)
                .owner(user)
                .name("nameItem")
                .description("qwe")
                .available(true)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(2L)
                .name("nameItem")
                .description("qwe")
                .available(true)
                .requestId(null)
                .build();

        booking = Booking.builder()
                .id(3L)
                .start(date.plusDays(1))
                .end(date.plusDays(3))
                .item(item)
                .booker(user2)
                .status(BookingStatus.WAITING)
                .build();

        bookingRestDto = BookingRestDto.builder()
                .itemId(2L)
                .start(date.plusDays(1))
                .end(date.plusDays(3))
                .build();

        bookingDto = BookingDto.builder()
                .id(3L)
                .start(date.plusDays(1))
                .end(date.plusDays(3))
                .item(itemDto)
                .booker(user2)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void toBookingDto() {
        BookingDto result = BookingMapper.toBookingDto(booking);

        assertEquals(bookingDto, result);
    }

    @Test
    void toBookingRestDto() {
        booking.setId(null);
        booking.setBooker(null);
        booking.setItem(null);
        Booking result = BookingRestMapper.toBooking(bookingRestDto);

        assertEquals(booking, result);
    }

    @Test
    void toLastNextBookingDto() {
        LastNextBookingDto lastNextBookingDto = new LastNextBookingDto(3L, 2L);
        LastNextBookingDto result = LastNextBookingMapper.mapBookingToLastNextDto(booking);

        assertEquals(lastNextBookingDto, result);
    }
}