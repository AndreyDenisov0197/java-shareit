package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestService;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    protected static final Sort SORT = Sort.by(Sort.Direction.DESC, "created");


    @Override
    public RequestDto createRequest(RequestDto requestDto, Long userId) {
        Request request = RequestMapper.toRequest(requestDto);
        request.setRequestor(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("There's no user with id " + userId)));
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public Collection<RequestWithItemsDto> getRequestsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("There's no user with id " + userId));
        return requestRepository.findByRequestorId(userId, SORT).stream()
                .map(RequestMapper::toRequestWithItemsDto)
                .peek(request ->  request.setItems(getListItems(request.getId())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Collection<RequestWithItemsDto> getAllRequest(Long userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);

        return requestRepository.findByRequestor_IdNotOrderByCreatedDesc(userId, pageRequest).stream()
                .map(RequestMapper::toRequestWithItemsDto)
                .peek(request ->  request.setItems(getListItems(request.getId())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public RequestWithItemsDto getRequestById(Long requestId, Long userId) {
        userRepository.checkUserById(userId);

        RequestWithItemsDto request = RequestMapper.toRequestWithItemsDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("There's no request with id " + requestId)));
        request.setItems(getListItems(request.getId()));
        return request;
    }

    private List<ItemDto> getListItems(Long requestId) {
        List<Item> items = itemRepository.findByRequestId(requestId);
        if (items.isEmpty()) {
            return List.of();
        }
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}