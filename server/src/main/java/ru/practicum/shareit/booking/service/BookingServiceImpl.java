package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    protected static final Sort SORT = Sort.by(Sort.Direction.DESC, "start");
    protected static final Sort SORT_ASC = Sort.by(Sort.Direction.ASC, "start");


    @Override
    public BookingDto bookingRequest(BookingRestDto bookingRestDto, Long booker) {
        Booking booking = BookingMapper.toBooking(bookingRestDto);
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
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, SORT);
        Collection<BookingDto> bookingDtos = List.of();

        switch (state) {
            case ALL:
                bookingDtos = toBookingDtoList(bookingRepository.findByBookerId(userId, pageRequest));
                break;

            case PAST:
                bookingDtos = toBookingDtoList(bookingRepository.findByBookerIdAndEndIsBefore(userId,
                        LocalDateTime.now(), pageRequest));
                break;

            case CURRENT:
                bookingDtos = toBookingDtoList(bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(userId,
                        LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(from, size, SORT_ASC)));
                break;

            case WAITING:
                bookingDtos =  toBookingDtoList(bookingRepository.findByBookerIdAndStatusIs(userId,
                        BookingStatus.WAITING, pageRequest));
                break;

            case FUTURE:
                bookingDtos =  toBookingDtoList(bookingRepository.findByBookerIdAndStartIsAfter(userId,
                        LocalDateTime.now(), pageRequest));
                break;

            case REJECTED:
                bookingDtos = toBookingDtoList(bookingRepository.findByBookerIdAndStatusIs(userId,
                        BookingStatus.REJECTED, pageRequest));
                break;
        }

        return bookingDtos;
    }

    @Override
    public Collection<BookingDto> getAllBookingForUserItems(BookingState state, Long userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, SORT);

        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) {
            throw new NotFoundException("There's no items belong to user " + userId);
        }

        List<Long> itemsId = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        Collection<BookingDto> bookingDtos = List.of();

        switch (state) {
            case ALL:
                bookingDtos = toBookingDtoList(bookingRepository.findByItemIdIn(itemsId, pageRequest));
                break;

            case PAST:
                bookingDtos = toBookingDtoList(bookingRepository.findByItemIdInAndEndIsBefore(itemsId,
                        LocalDateTime.now(), pageRequest));
                break;

            case CURRENT:
                bookingDtos = toBookingDtoList(bookingRepository.findByItemIdInAndEndIsAfterAndStartIsBefore(itemsId,
                        LocalDateTime.now(), LocalDateTime.now(), pageRequest));
                break;

            case WAITING:
                bookingDtos = toBookingDtoList(bookingRepository.findByItemIdInAndStatusIs(
                        itemsId, BookingStatus.WAITING, pageRequest));
                break;

            case FUTURE:
                bookingDtos = toBookingDtoList(bookingRepository.findByItemIdInAndStartIsAfter(itemsId,
                        LocalDateTime.now(), pageRequest));
                break;

            case REJECTED:
                bookingDtos = toBookingDtoList(bookingRepository.findByItemIdInAndStatusIs(itemsId,
                        BookingStatus.REJECTED, pageRequest));
                break;
        }

        return bookingDtos;
    }

    private Collection<BookingDto> toBookingDtoList(Collection<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}