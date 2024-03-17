package ru.practicum.shareit.item.dto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OutgoingItemDtoTest {

    @Autowired
    private JacksonTester<OutgoingItemDto> json;

    @Test
    void testOutgoingItemDto() throws IOException {
        LocalDateTime date = LocalDateTime.now();

        OutgoingItemDto item = OutgoingItemDto.builder()
                .id(1L)
                .name("name")
                .description("qwerty")
                .available(false)
                .lastBooking(new LastNextBookingDto())
                .nextBooking(new LastNextBookingDto())
                .comments(List.of(new ItemCommentsDto()))
                .build();

        JsonContent<OutgoingItemDto> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("qwerty");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
    }
}
