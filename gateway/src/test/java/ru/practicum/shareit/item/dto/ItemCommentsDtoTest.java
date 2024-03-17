package ru.practicum.shareit.item.dto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemCommentsDtoTest {

    @Autowired
    private JacksonTester<ItemCommentsDto> json;

    @Test
    void toItemCommentsDto() throws IOException {
        LocalDateTime date = LocalDateTime.now();

        ItemCommentsDto item = ItemCommentsDto.builder()
                .id(1L)
                .text("text")
                .authorName("name")
                .created(date)
                .build();

        JsonContent<ItemCommentsDto> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("name");
    }
}
