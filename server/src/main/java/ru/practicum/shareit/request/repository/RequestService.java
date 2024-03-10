package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;

import java.util.Collection;

public interface RequestService {
    RequestDto createRequest(RequestDto request, Long userId);

    Collection<RequestWithItemsDto> getRequestsByUserId(Long userId);

    Collection<RequestWithItemsDto> getAllRequest(Long userId, int from, int size);

    RequestWithItemsDto getRequestById(Long requestId, Long userId);
}
