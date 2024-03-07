package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestWithItemsDtoTest {
    @Autowired
    private JacksonTester<RequestWithItemsDto> json;

    @Test
    void toRequestWithItemsDto() throws IOException {
        LocalDateTime date = LocalDateTime.now();

        RequestWithItemsDto item = RequestWithItemsDto.builder()
                .id(1L)
                .description("text")
                .created(date)
                .items(List.of(new ItemDto()))
                .build();

        JsonContent<RequestWithItemsDto> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("text");
    }

}