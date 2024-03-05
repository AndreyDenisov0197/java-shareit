package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.pageRequest.MyPageRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.mapper.RequestWithItemsMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.request.service.RequestServiceDb.SORT;

@ExtendWith(MockitoExtension.class)
class RequestServiceDbTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private RequestServiceDb requestService;
    private RequestDto requestDto;
    private User user;
    private Request request;
    private RequestDto requestUpdate;
    private RequestWithItemsDto requestWithItemsDto;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("userName")
                .email("email@mail.ru")
                .build();

        request = Request.builder()
                .id(2L)
                .description("text")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        requestDto = RequestMapper.toRequestDto(request);

        requestUpdate = RequestDto.builder()
                .description("text")
                .build();

        requestWithItemsDto = RequestWithItemsMapper.toRequestWithItemsDto(request);
        requestWithItemsDto.setItems(List.of());
    }


    @Test
    void createRequest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        RequestDto result = requestService.createRequest(requestUpdate, user.getId());

        assertEquals(requestDto, result);
        verify(userRepository).findById(user.getId());
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void createRequest_whenUserIdNotFound() {
        long userId = 4L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.createRequest(requestUpdate, userId));

        verify(userRepository).findById(anyLong());
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void getRequestsByUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.findByRequestorId(user.getId(), SORT)).thenReturn(List.of(request));
        when(itemRepository.findByRequestId(request.getId())).thenReturn(List.of());

        Collection<RequestWithItemsDto> result = requestService.getRequestsByUserId(user.getId());

        assertEquals(List.of(requestWithItemsDto), result);
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findByRequestId(anyLong());
        verify(requestRepository).findByRequestorId(user.getId(), SORT);
    }

    @Test
    void getRequestsByUserId_whenUserIdNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequestsByUserId(user.getId()));

        verify(userRepository).findById(anyLong());
        verify(requestRepository, never()).findByRequestorId(user.getId(), SORT);
    }

    @Test
    void getAllRequest() {
        int from = 0;
        int size = 20;
        when(requestRepository.findByRequestorIdNotLike(user.getId(), new MyPageRequest(from, size, SORT)))
                .thenReturn(List.of(request));
        when(itemRepository.findByRequestId(request.getId())).thenReturn(List.of());

        Collection<RequestWithItemsDto> result = requestService.getAllRequest(user.getId(), from, size);

        assertEquals(List.of(requestWithItemsDto), result);
        verify(itemRepository).findByRequestId(anyLong());
        verify(requestRepository).findByRequestorIdNotLike(user.getId(), new MyPageRequest(from, size, SORT));
    }

    @Test
    void getRequestById() {
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));

        RequestWithItemsDto result = requestService.getRequestById(request.getId(), user.getId());

        assertEquals(requestWithItemsDto, result);
        verify(requestRepository).findById(request.getId());
    }


    @Test
    void getRequestById_whenRequestIdNotFound() {
        when(requestRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(request.getId(), user.getId()));

        verify(requestRepository).findById(request.getId());
    }
}