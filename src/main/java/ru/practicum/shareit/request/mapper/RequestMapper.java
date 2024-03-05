package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;

public class RequestMapper {

    public static Request toRequest(RequestDto requestDto) {
        return new Request(
                null,
                requestDto.getDescription(),
                null,
                LocalDateTime.now()
        );
    }

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor().getId(),
                request.getCreated()
        );
    }
}
