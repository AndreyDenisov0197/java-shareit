package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class LastNextBookingDtoTest {
    @Autowired
    private JacksonTester<LastNextBookingDto> json;

    @Test
    void testLastNextBookingDto() throws Exception {
        LastNextBookingDto booking = new LastNextBookingDto(1L, 2L);

        JsonContent<LastNextBookingDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
    }
}