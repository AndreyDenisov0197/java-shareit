package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRestDtoTest {

    @Autowired
    private JacksonTester<BookingRestDto> json;

    @Test
    void testBookingRestDto() throws Exception {
        LocalDateTime date = LocalDateTime.now();

        BookingRestDto booking = BookingRestDto.builder()
                .itemId(1L)
                .start(date.plusHours(2))
                .end(date.plusDays(2))
                .build();

        JsonContent<BookingRestDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}