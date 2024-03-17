package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestDtoTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void toItemCommentsDto() throws IOException {
        LocalDateTime date = LocalDateTime.now();

        RequestDto item = RequestDto.builder()
                .id(1L)
                .description("text")
                .requestor(2L)
                .created(date)
                .build();

        JsonContent<RequestDto> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("text");
        assertThat(result).extractingJsonPathNumberValue("$.requestor").isEqualTo(2);
    }
}