package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.Request;

@RequiredArgsConstructor
public class RequestWithItemsMapper {

    public static RequestWithItemsDto toRequestWithItemsDto(Request request) {
        return new RequestWithItemsDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                null
        );
    }
}
