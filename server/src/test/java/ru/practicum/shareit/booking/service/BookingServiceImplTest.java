package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.service.BookingServiceImpl.SORT;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl service;

    private User user;
    private User user2;
    private Item item;
    private Booking booking;
    private BookingRestDto bookingRestDto;
    private BookingDto bookingDto;
    private int from = 0;
    private int size = 20;
    private PageRequest pageRequest;
    private List<Long> itemsId;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("userName")
                .email("email@mail.ru")
                .build();

        user2 = User.builder()
                .id(2L)
                .name("user")
                .email("email2@mail.ru")
                .build();

        item = Item.builder()
                .id(2L)
                .owner(user)
                .name("nameItem")
                .description("qwe")
                .available(true)
                .build();

        booking = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user2)
                .status(BookingStatus.WAITING)
                .build();

        bookingRestDto = BookingRestDto.builder()
                .itemId(2L)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(4))
                .build();

        bookingDto = BookingMapper.toBookingDto(booking);
        pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, SORT);
        itemsId = List.of(item.getId());

    }

    @Test
    void bookingRequest() {
        Booking bookingAdd = BookingMapper.toBooking(bookingRestDto);
        bookingAdd.setItem(item);
        bookingAdd.setBooker(user2);

        when(itemRepository.findById(bookingRestDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(bookingRepository.save(bookingAdd)).thenReturn(booking);

        BookingDto result = service.bookingRequest(bookingRestDto, user2.getId());

        assertEquals(bookingDto.getItem(), result.getItem());
        verify(itemRepository).findById(item.getId());
        verify(userRepository).findById(user2.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void bookingRequest_whenUserNotFound() {
        when(itemRepository.findById(bookingRestDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.bookingRequest(bookingRestDto, user2.getId()));

        verify(itemRepository).findById(anyLong());
        verify(userRepository).findById(user2.getId());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookingRequest_whenItemIsAvailableFalse() {
        item.setAvailable(false);
        when(itemRepository.findById(bookingRestDto.getItemId())).thenReturn(Optional.of(item));

        assertThrows(NotAvailableItemException.class,
                () -> service.bookingRequest(bookingRestDto, user2.getId()));

        verify(itemRepository).findById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookingRequest_whenUserIsOwnerItem() {
        when(itemRepository.findById(bookingRestDto.getItemId())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class,
                () -> service.bookingRequest(bookingRestDto, user.getId()));

        verify(itemRepository).findById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookingRequest_whenItemIdNotFound() {
        bookingRestDto.setItemId(35L);

        when(itemRepository.findById(bookingRestDto.getItemId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.bookingRequest(bookingRestDto, user2.getId()));

        verify(itemRepository).findById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookingConfirmation_whenStatusTrue() {
        Booking bookingApproved = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user2)
                .status(BookingStatus.APPROVED)
                .build();
        BookingDto bookingDtoApproved = BookingMapper.toBookingDto(bookingApproved);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(bookingApproved);

        BookingDto result = service.bookingConfirmation(booking.getId(), user.getId(), true);

        assertEquals(bookingDtoApproved.getStatus(), result.getStatus());
        assertEquals(bookingDtoApproved, result);
        verify(bookingRepository).findById(booking.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void bookingConfirmation_whenStatusFalse() {
        Booking bookingApproved = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user2)
                .status(BookingStatus.REJECTED)
                .build();
        BookingDto bookingDtoApproved = BookingMapper.toBookingDto(bookingApproved);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(bookingApproved);

        BookingDto result = service.bookingConfirmation(booking.getId(), user.getId(), false);

        assertEquals(bookingDtoApproved.getStatus(), result.getStatus());
        assertEquals(bookingDtoApproved, result);
        verify(bookingRepository).findById(booking.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void bookingConfirmation_whenBookingStatusNotWaiting() {
        booking.setStatus(BookingStatus.CANCELED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotAvailableItemException.class,
                () -> service.bookingConfirmation(booking.getId(), user.getId(), true));

        verify(bookingRepository).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookingConfirmation_whenUserIdDoNotBelongItem() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class,
                () -> service.bookingConfirmation(booking.getId(), user2.getId(), true));

        verify(bookingRepository).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void bookingConfirmation_whenBookingIdNotFound() {
        when(bookingRepository.findById(35L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.bookingConfirmation(35L, user.getId(), true));

        verify(bookingRepository).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void getBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItem().getId())).thenReturn(Optional.of(item));

        BookingDto result = service.getBooking(booking.getId(), user.getId());

        assertEquals(bookingDto.getStatus(), result.getStatus());
        assertEquals(bookingDto, result);
        verify(bookingRepository).findById(booking.getId());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void getBooking_whenBookingIdNotFound() {
        when(bookingRepository.findById(35L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getBooking(35L, user.getId()));

        verify(bookingRepository).findById(anyLong());
        verify(itemRepository, never()).findById(anyLong());
    }

    @Test
    void getBooking_whenItemNotFound() {
        booking.setItem(Item.builder().id(35L).build());
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItem().getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getBooking(booking.getId(), user.getId()));

        verify(bookingRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void getBooking_whenUserDoNotOwnerAndBooker() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(booking.getItem().getId())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class,
                () -> service.getBooking(booking.getId(), 35L));

        verify(bookingRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void getAllBookingForBooker_whenStateAll() {
        when(bookingRepository.findByBookerId(user2.getId(), pageRequest)).thenReturn(List.of(booking));

        Collection<BookingDto> result = service.getAllBookingForBooker(BookingState.ALL, user2.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByBookerId(user2.getId(), pageRequest);
    }

    @Test
    void getAllBookingForBooker_whenStatePast() {
        when(bookingRepository.findByBookerIdAndEndIsBefore(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        Collection<BookingDto> result = service.getAllBookingForBooker(BookingState.PAST, user2.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByBookerIdAndEndIsBefore(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingForBooker_whenStateCurrent() {
        when(bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        Collection<BookingDto> result = service.getAllBookingForBooker(BookingState.CURRENT, user2.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByBookerIdAndEndIsAfterAndStartIsBefore(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingForBooker_whenStateWaiting() {
        when(bookingRepository.findByBookerIdAndStatusIs(user2.getId(), BookingStatus.WAITING,
                pageRequest)).thenReturn(List.of(booking));

        Collection<BookingDto> result = service.getAllBookingForBooker(BookingState.WAITING, user2.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByBookerIdAndStatusIs(user2.getId(), BookingStatus.WAITING,
                pageRequest);
    }

    @Test
    void getAllBookingForBooker_whenStateFuture() {
        when(bookingRepository.findByBookerIdAndStartIsAfter(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        Collection<BookingDto> result = service.getAllBookingForBooker(BookingState.FUTURE, user2.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByBookerIdAndStartIsAfter(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingForBooker_whenStateRejected() {
        when(bookingRepository.findByBookerIdAndStatusIs(user2.getId(),
                BookingStatus.REJECTED, pageRequest)).thenReturn(List.of(booking));

        Collection<BookingDto> result =
                service.getAllBookingForBooker(BookingState.REJECTED, user2.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByBookerIdAndStatusIs(user2.getId(),
                BookingStatus.REJECTED, pageRequest);
    }

    @Test
    void getAllBookingForUserItems_whenStateAll() {
        List<Long> itemsId = List.of(item.getId());
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdIn(itemsId, pageRequest)).thenReturn(List.of(booking));

        Collection<BookingDto> result =
                service.getAllBookingForUserItems(BookingState.ALL, user.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByItemIdIn(itemsId, pageRequest);
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllBookingForUserItems_whenUserNotHaveItem() {
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of());

        assertThrows(NotFoundException.class,
                () -> service.getAllBookingForUserItems(BookingState.ALL, user.getId(), from, size));

        verify(bookingRepository, never()).findByItemIdIn(itemsId, pageRequest);
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllBookingForUserItems_whenStatePast() {
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdInAndEndIsBefore(anyList(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        Collection<BookingDto> result =
                service.getAllBookingForUserItems(BookingState.PAST, user.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByItemIdInAndEndIsBefore(anyList(),
                any(LocalDateTime.class), any(PageRequest.class));
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllBookingForUserItems_whenStateCurrent() {
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdInAndEndIsAfterAndStartIsBefore(anyList(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        Collection<BookingDto> result =
                service.getAllBookingForUserItems(BookingState.CURRENT, user.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByItemIdInAndEndIsAfterAndStartIsBefore(anyList(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllBookingForUserItems_whenStateWaiting() {
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdInAndStatusIs(itemsId,
                BookingStatus.WAITING, pageRequest)).thenReturn(List.of(booking));

        Collection<BookingDto> result =
                service.getAllBookingForUserItems(BookingState.WAITING, user.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByItemIdInAndStatusIs(itemsId,
                BookingStatus.WAITING, pageRequest);
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllBookingForUserItems_whenStateFuture() {
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdInAndStartIsAfter(anyList(),
                any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(booking));

        Collection<BookingDto> result =
                service.getAllBookingForUserItems(BookingState.FUTURE, user.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByItemIdInAndStartIsAfter(anyList(),
                any(LocalDateTime.class), any(PageRequest.class));
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllBookingForUserItems_whenStateRejected() {
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdInAndStatusIs(itemsId,
                BookingStatus.REJECTED, pageRequest)).thenReturn(List.of(booking));

        Collection<BookingDto> result =
                service.getAllBookingForUserItems(BookingState.REJECTED, user.getId(), from, size);

        assertEquals(List.of(bookingDto), result);
        assertEquals(List.of(bookingDto).size(), result.size());
        verify(bookingRepository).findByItemIdInAndStatusIs(itemsId,
                BookingStatus.REJECTED, pageRequest);
        verify(itemRepository).findByOwnerId(user.getId());
    }


    @Test
    void testMyPageRequestGetOffset() {
        long result = pageRequest.getOffset();
        assertEquals(from, result);
    }

    @Test
    void mapBookingToLastNextDto() {
        assertEquals(new LastNextBookingDto(3L, 2L),
                BookingMapper.mapBookingToLastNextDto(booking));
    }
}