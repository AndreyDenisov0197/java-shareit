package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.LastNextBookingDto;
import ru.practicum.shareit.booking.mapper.LastNextBookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemCommentsMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.OutgoingItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceDb implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("There's no user with id " + userId)));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("There's no items with id " + itemId));


        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не является владельцем элемента!");
        }

        String name = itemDto.getName();
        if (name != null && !item.getName().equals(name)) {
            item.setName(name);
        }

        String description = itemDto.getDescription();
        if (description != null && !item.getDescription().equals(description)) {
            item.setDescription(description);
        }

        Boolean available = itemDto.getAvailable();
        if (available != item.getAvailable() && available != null) {
            item.setAvailable(available);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public OutgoingItemDto getItemById(Long itemId, Long userId) {
        userRepository.checkUserById(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("There's no item with id " + itemId));
        OutgoingItemDto outgoingItemDto = OutgoingItemMapper.toOutgoingItemCollection(item);

        if (item.getOwner().getId().equals(userId)) {
            outgoingItemDto.setLastBooking(getLastBookingByItemId(itemId));
            outgoingItemDto.setNextBooking(getNextBookingByItemId(itemId));
        }
        outgoingItemDto.setComments(getComments(itemId));
        return outgoingItemDto;
    }

    @Override
    public Collection<OutgoingItemDto> getItemsByUser(Long userId) {
        userRepository.checkUserById(userId);
        Collection<OutgoingItemDto> items = itemRepository.findByOwnerId(userId).stream()
                .map(OutgoingItemMapper::toOutgoingItemCollection)
                .collect(Collectors.toList());

        Collection<OutgoingItemDto> outgoingItems = new ArrayList<>();

        for (OutgoingItemDto itemDto : items) {
            long id = itemDto.getId();
            itemDto.setLastBooking(getLastBookingByItemId(id));
            itemDto.setNextBooking(getNextBookingByItemId(id));
            itemDto.setComments(getComments(id));
            outgoingItems.add(itemDto);
        }

        return outgoingItems;
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .filter(i -> i.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    @Override
    public ItemCommentsDto createComments(Long itemId, Long userId, CommentDto commentDto) {
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(userId,
                itemId, LocalDateTime.now(), BookingStatus.APPROVED)) {
            throw new NotAvailableItemException("Author with id " + userId
                    + " has no rights to leve a comment to item with id " + itemId + "!");
        }
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(itemRepository.findById(itemId).orElseThrow());
        comment.setAuthor(userRepository.findById(userId).orElseThrow());
        return ItemCommentsMapper.toItemComments(commentRepository.save(comment));
    }

    private LastNextBookingDto getNextBookingByItemId(Long itemId) {
        Booking booking = bookingRepository.findFirstByItemIdAndStartIsAfterAndStatusIs(itemId, LocalDateTime.now(),
                BookingStatus.APPROVED, Sort.by(Sort.Direction.ASC, "start")).orElse(null);
        return booking != null ? LastNextBookingMapper.mapBookingToLastNextDto(booking) : null;
    }

    private LastNextBookingDto getLastBookingByItemId(Long itemId) {
        Booking booking = bookingRepository.findFirstByItemIdAndStartIsBeforeAndStatusIs(itemId, LocalDateTime.now(),
                BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "end")).orElse(null);
        return booking != null ? LastNextBookingMapper.mapBookingToLastNextDto(booking) : null;
    }

    private List<ItemCommentsDto> getComments(Long itemId) {
         return commentRepository.findByItemId(itemId).stream()
                .map(ItemCommentsMapper::toItemComments)
                .collect(Collectors.toList());
    }
}
