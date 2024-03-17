package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookItemRequestDtoTest {

    @Autowired
    private JacksonTester<BookItemRequestDto> json;

    @Test
    void testBookingRestDto() throws Exception {
        LocalDateTime date = LocalDateTime.now();

        BookItemRequestDto booking = BookItemRequestDto.builder()
                .itemId(1L)
                .start(date.plusHours(2))
                .end(date.plusDays(2))
                .build();

        JsonContent<BookItemRequestDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}