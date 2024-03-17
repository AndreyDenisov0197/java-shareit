package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private Comment comment;
    private OutgoingItemDto outgoingItemDto;
    private ItemCommentsDto itemCommentsDto;
    private final int from = 0;
    private final int size = 20;
    private PageRequest pageRequest;

    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "nameUser", "email@mail.ru");
        item = new Item(2L, user, "nameItem", "qwe", true, null);
        itemDto = ItemMapper.toItemDto(item);
        comment = new Comment(3L, "nameComment", item, user, LocalDateTime.now());
        outgoingItemDto = ItemMapper.toOutgoingItemCollection(item);
        outgoingItemDto.setComments(List.of());
        itemCommentsDto = CommentMapper.toItemComments(comment);
        pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
    }

    @Test
    void createItem() {
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ItemDto result = itemService.createItem(user.getId(), itemDto);

        assertEquals(itemDto, result);
        verify(itemRepository).save(any(Item.class));
        verify(userRepository).findById(user.getId());
        verify(requestRepository, never()).findById(anyLong());
    }

    @Test
    void createItem_whenUserIdNotFound() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(userId, itemDto));

        verify(itemRepository, never()).save(any(Item.class));
        verify(userRepository).findById(userId);
        verify(requestRepository, never()).findById(anyLong());
    }

    @Test
    void createItem_whenRequestIdNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        itemDto.setRequestId(7L);

        assertThrows(NotFoundException.class, () -> itemService.createItem(user.getId(), itemDto));

        verify(itemRepository, never()).save(any(Item.class));
        verify(userRepository).findById(user.getId());
        verify(requestRepository).findById(anyLong());
    }

    @Test
    void updateItem() {
        ItemDto itemToUpdate = new ItemDto(null, "name1", "qwe1", false, null);
        Item updateItem = new Item(2L, user, "name1", "qwe1", false, null);
        ItemDto expectedItem = ItemMapper.toItemDto(updateItem);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(updateItem)).thenReturn(updateItem);

        ItemDto result = itemService.updateItem(user.getId(), itemToUpdate, item.getId());

        assertEquals(expectedItem, result);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(updateItem, itemArgumentCaptor.getValue());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void updateItem_whenUpdateName() {
        ItemDto itemToUpdate = new ItemDto(null, "name", null, null, null);
        Item updateItem = new Item(2L, user, "name", "qwe", true, null);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(updateItem)).thenReturn(updateItem);
        ItemDto expectedItem = ItemMapper.toItemDto(updateItem);

        ItemDto result = itemService.updateItem(user.getId(), itemToUpdate, item.getId());

        assertEquals(expectedItem, result);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        assertEquals(updateItem, itemArgumentCaptor.getValue());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void updateItem_whenUserIdNoyFound() {
        ItemDto itemToUpdate = new ItemDto(null, "name", "qwe", true, null);
        long userId = 5L;
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(userId, itemToUpdate, item.getId()));

        verify(itemRepository, never()).save(any(Item.class));
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void updateItem_whenItemIdNoyFound() {
        long userId = 3L;
        long itemId = 5L;
        ItemDto itemToUpdate = new ItemDto(null, "name", "qwe", true, null);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(userId, itemToUpdate, itemId));

        verify(itemRepository, never()).save(any(Item.class));
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void getItemById() {
        long itemId = item.getId();
        long userId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(itemId)).thenReturn(List.of());

        OutgoingItemDto result = itemService.getItemById(itemId, userId);

        assertEquals(outgoingItemDto, result);
        verify(itemRepository).findById(itemId);
        verify(commentRepository).findByItemId(itemId);
    }

    @Test
    void getItemById_whenItemIdNotFound() {
        long itemId = 2L;
        long userId = 3L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(itemId, userId));

        verify(itemRepository).findById(itemId);
        verify(commentRepository, never()).findByItemId(itemId);
    }

/*    @Test
    void getItemsByUser() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest myPageRequest = PageRequest.of(from, size, sort);

        when(itemRepository.findByOwnerId(user.getId(), myPageRequest)).thenReturn(List.of(item));
        Collection<OutgoingItemDto> collectionItems = List.of(outgoingItemDto);

        Collection<OutgoingItemDto> result = itemService.getItemsByUser(user.getId(), from, size);

        assertEquals(collectionItems, result);
        verify(itemRepository).findByOwnerId(user.getId(), pageRequest);
    }*/

    @Test
    void searchItem() {
        String text = "name";
        Item item2 = new Item(2L, user, "name2", "qwe2", false, null);
        when(itemRepository.search(text, pageRequest)).thenReturn(List.of(item, item2));

        Collection<ItemDto> result = itemService.searchItem(text, from, size);

        assertEquals(List.of(itemDto), result);
        verify(itemRepository).search(text, pageRequest);
    }

    @Test
    void searchItem_whenTextIsBlank() {
        String text = "    ";

        Collection<ItemDto> result = itemService.searchItem(text, from, size);

        assertEquals(new ArrayList<>(), result);
        verify(itemRepository, never()).search(text, pageRequest);
    }

    @Test
    void createComments() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.existsByBooker_IdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(),
                anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        ItemCommentsDto result = itemService.createComments(item.getId(), user.getId(), CommentMapper.toCommentDto(comment));

        assertEquals(itemCommentsDto, result);
        verify(itemRepository).findById(item.getId());
        verify(userRepository).findById(user.getId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComments_whenUserNotBooking() {
        when(bookingRepository.existsByBooker_IdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(), anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(false);

        assertThrows(NotAvailableItemException.class, () -> itemService.createComments(item.getId(), user.getId(),
                CommentMapper.toCommentDto(comment)));
    }

    @Test
    void createComments_whenItemIdNotFound() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());
        when(bookingRepository.existsByBooker_IdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(),
                anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(true);

        assertThrows(NotFoundException.class, () -> itemService.createComments(item.getId(), user.getId(),
                CommentMapper.toCommentDto(comment)));
    }

    @Test
    void createComments_whenUserIdNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.existsByBooker_IdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(),
                anyLong(), any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(true);

        assertThrows(NotFoundException.class, () -> itemService.createComments(item.getId(), user.getId(),
                CommentMapper.toCommentDto(comment)));
    }
}