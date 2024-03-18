package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestMapperTest {
    private Request request;
    private RequestDto requestDto;

    private RequestWithItemsDto requestWithItemsDto;

    @BeforeEach
    public void beforeEach() {
        LocalDateTime date = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .name("userName")
                .email("email@mail.ru")
                .build();

        request = Request.builder()
                .id(1L)
                .description("qwerty")
                .requestor(user)
                .created(date)
                .build();

        requestDto = RequestDto.builder()
                .id(1L)
                .description("qwerty")
                .requestor(user.getId())
                .created(date)
                .build();

        requestWithItemsDto = RequestWithItemsDto.builder()
                .id(1L)
                .description("qwerty")
                .created(date)
                .build();
    }

    @Test
    void toRequest() {
        Request result = RequestMapper.toRequest(requestDto);
        request.setId(null);
        request.setRequestor(null);
        request.setCreated(result.getCreated());

        assertEquals(request, result);
    }

    @Test
    void toRequestDto() {
        RequestDto result = RequestMapper.toRequestDto(request);
        requestDto.setCreated(result.getCreated());

        assertEquals(requestDto, result);
    }

    @Test
    void toRequestWithItemsDto() {
        RequestWithItemsDto result = RequestMapper.toRequestWithItemsDto(request);

        assertEquals(requestWithItemsDto, result);
    }
}