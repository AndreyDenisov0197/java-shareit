package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.BookingRestMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.pageRequest.MyPageRequest;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceDb implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    protected static final Sort SORT = Sort.by(Sort.Direction.DESC, "start");
    protected static final Sort SORT_ASC = Sort.by(Sort.Direction.ASC, "start");


    @Override
    public BookingDto bookingRequest(BookingRestDto bookingRestDto, Long booker) {
        Booking booking = BookingRestMapper.toBooking(bookingRestDto);
        Item item = itemRepository.findById(bookingRestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("There's no item with id " + bookingRestDto.getItemId()));

        if (item.getOwner().getId().equals(booker))
            throw new NotFoundException("Booking item belongs to booker!");
        if (!item.getAvailable()) {
            throw new NotAvailableItemException("Booking item is not available!");
        }

        booking.setItem(item);
        booking.setBooker(userRepository.findById(booker)
                .orElseThrow(() -> new NotFoundException("There's no user with id " + booker)));
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto bookingConfirmation(Long bookingId, Long userId, boolean status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Нет бронирования с ID " + bookingId));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Booking item don't belong to user with id " + userId);
        }

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new NotAvailableItemException("Not allowed to change booking status " + booking.getStatus());
        }

        if (status) {
            booking.setStatus(BookingStatus.APPROVED); // есть возможность добавить изменение статуса для Item
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        BookingDto booking = BookingMapper.toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("There's no booking with id " + bookingId)));

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("There's no item with id " + booking.getItem().getId()));

        if (!booking.getBooker().getId().equals(userId) && !item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не является владельцем и пользователем элемента!");
        }
        return booking;
    }

    @Override
    public Collection<BookingDto> getAllBookingForBooker(BookingState state, Long userId, int from, int size) {
        userRepository.checkUserById(userId);
        MyPageRequest pageRequest = new MyPageRequest(from, size, SORT);
        switch (state) {
            case ALL:
                return toBookingDtoList(bookingRepository.findByBookerId(userId, pageRequest));

            case PAST:
                return toBookingDtoList(bookingRepository.findByBookerIdAndEndIsBefore(userId,
                        LocalDateTime.now(), pageRequest));

            case CURRENT:
                return toBookingDtoList(bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(userId,
                        LocalDateTime.now(), LocalDateTime.now(), new MyPageRequest(from, size, SORT_ASC)));

            case WAITING:
                return toBookingDtoList(bookingRepository.findByBookerIdAndStatusIs(userId,
                        BookingStatus.WAITING, pageRequest));

            case FUTURE:
                return toBookingDtoList(bookingRepository.findByBookerIdAndStartIsAfter(userId,
                        LocalDateTime.now(), pageRequest));

            case REJECTED:
                return toBookingDtoList(bookingRepository.findByBookerIdAndStatusIs(userId,
                        BookingStatus.REJECTED, pageRequest));

            default:
                return List.of();
        }
    }

    @Override
    public Collection<BookingDto> getAllBookingForUserItems(BookingState state, Long userId, int from, int size) {
        MyPageRequest pageRequest = new MyPageRequest(from, size, SORT);


        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) {
            throw new NotFoundException("There's no items belong to user " + userId);
        }

        List<Long> itemsId = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());


        switch (state) {
            case ALL:
                return toBookingDtoList(bookingRepository.findByItemIdIn(itemsId, pageRequest));

            case PAST:
                return toBookingDtoList(bookingRepository.findByItemIdInAndEndIsBefore(itemsId,
                        LocalDateTime.now(), pageRequest));

            case CURRENT:
                return toBookingDtoList(bookingRepository.findByItemIdInAndEndIsAfterAndStartIsBefore(itemsId,
                        LocalDateTime.now(), LocalDateTime.now(), pageRequest));

            case WAITING:
                return toBookingDtoList(bookingRepository.findByItemIdInAndStatusIs(itemsId,
                        BookingStatus.WAITING, pageRequest));

            case FUTURE:
                return toBookingDtoList(bookingRepository.findByItemIdInAndStartIsAfter(itemsId,
                        LocalDateTime.now(), pageRequest));

            case REJECTED:
                return toBookingDtoList(bookingRepository.findByItemIdInAndStatusIs(itemsId,
                        BookingStatus.REJECTED, pageRequest));

            default:
                return List.of();
        }
    }

    private Collection<BookingDto> toBookingDtoList(Collection<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
