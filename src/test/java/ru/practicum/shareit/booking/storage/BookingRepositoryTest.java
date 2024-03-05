package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user;
    private User user2;
    private Item item;
    private Item item2;
    private Booking booking;
    private Booking booking2;
    private Booking booking3;
    private Sort sort;
    private Sort sortAsc;
    private List<Long> itemsId;

    @BeforeEach
    public void beforeEach() {
        User userToSave = User.builder().name("name").email("email@email.com").build();
        user = userRepository.save(userToSave);

        User userToSave2 = User.builder().name("nameUser").email("email2@email.com").build();
        user2 = userRepository.save(userToSave2);

        Item itemToSave = Item.builder().name("name").description("description")
                .available(true).owner(user).build();
        item = itemRepository.save(itemToSave);

        Booking bookingToSave = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(user2)
                .status(BookingStatus.CANCELED)
                .build();
        booking = bookingRepository.save(bookingToSave);

        Item itemToSave2 = Item.builder().name("script").description("text")
                .available(true).owner(user).build();
        item2 = itemRepository.save(itemToSave2);

        Booking bookingToSave2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(2))
                .item(item2)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        booking2 = bookingRepository.save(bookingToSave2);

        Booking bookingToSave3 = Booking.builder()
                .start(LocalDateTime.now().minusDays(5))
                .end(LocalDateTime.now().plusDays(3))
                .item(item2)
                .booker(user)
                .status(BookingStatus.CANCELED)
                .build();
        booking3 = bookingRepository.save(bookingToSave3);

        sort = Sort.by(Sort.Direction.DESC, "start");
        sortAsc = Sort.by(Sort.Direction.ASC, "start");
        itemsId = List.of(item.getId(), item2.getId());
    }

    @AfterEach
    public void deleteItems() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByBookerId() {
        Collection<Booking> result =
                bookingRepository.findByBookerId(user.getId(), PageRequest.of(0,10, sort));

        assertEquals(List.of(booking2, booking3), result);
        assertEquals(2, result.size());
    }

    @Test
    void findByBookerId_whenPageRequestFrom1Size1() {
        Collection<Booking> result =
                bookingRepository.findByBookerId(user.getId(), PageRequest.of(1,1, sort));

        assertEquals(List.of(booking3), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerIdAndEndIsBefore() {
        Collection<Booking> result =
                bookingRepository.findByBookerIdAndEndIsBefore(
                        user.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0,10, sortAsc));

        assertEquals(List.of(booking2), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerIdAndEndIsBefore_whenEndNotBefore() {
        booking2.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking2);
        Collection<Booking> result =
                bookingRepository.findByBookerIdAndEndIsBefore(
                        user.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0,3, sortAsc));

        assertNotEquals(List.of(booking2), result);
        assertEquals(0, result.size());
    }

    @Test
    void findByBookerIdAndEndIsAfterAndStartIsBefore() {
        booking.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking);

        Collection<Booking> result =
                bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(
                        user2.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        PageRequest.of(0,3, sort));

        assertEquals(List.of(booking), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerIdAndEndIsAfterAndStartIsBefore_whenStartNotIsBefore() {
        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(6));
        bookingRepository.save(booking);

        Collection<Booking> result =
                bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(
                        user2.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        PageRequest.of(0,3, sort));

        assertEquals(List.of(), result);
        assertEquals(0, result.size());
    }

    @Test
    void findByBookerIdAndStatusIs_whenStatusIsApproved() {
        Collection<Booking> result =
                bookingRepository.findByBookerIdAndStatusIs(
                        user.getId(),
                        BookingStatus.APPROVED,
                        PageRequest.of(0,5));

        assertEquals(List.of(booking2), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerIdAndStatusIs_whenStatusIsCanceled() {
        Collection<Booking> result =
                bookingRepository.findByBookerIdAndStatusIs(
                        user.getId(),
                        BookingStatus.CANCELED,
                        PageRequest.of(0,5));

        assertEquals(List.of(booking3), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerIdAndStartIsAfter_whenStartNotIsAfter() {
        Collection<Booking> result =
                bookingRepository.findByBookerIdAndStartIsAfter(
                        user.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0,5));

        assertEquals(List.of(), result);
        assertEquals(0, result.size());
    }

    @Test
    void findByBookerIdAndStartIsAfter_whenStartIsAfter() {
        booking3.setStart(LocalDateTime.now().plusDays(1));
        bookingRepository.save(booking3);

        Collection<Booking> result =
                bookingRepository.findByBookerIdAndStartIsAfter(
                        user.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0,5));

        assertEquals(List.of(booking3), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByItemIdIn() {
        Collection<Booking> result =
                bookingRepository.findByItemIdIn(
                        List.of(item.getId(), item2.getId()),
                        PageRequest.of(0,5, sort));

        assertEquals(List.of(booking, booking2, booking3), result);
        assertEquals(3, result.size());
    }

    @Test
    void findByItemIdIn_whenPageRequestAsc() {
        Collection<Booking> result =
                bookingRepository.findByItemIdIn(
                        itemsId,
                        PageRequest.of(0,5, sortAsc));

        assertEquals(List.of(booking3, booking2, booking), result);
        assertEquals(3, result.size());
    }

    @Test
    void findByItemIdInAndEndIsBefore() {
        Collection<Booking> result =
                bookingRepository.findByItemIdInAndEndIsBefore(
                        itemsId,
                        LocalDateTime.now(),
                        PageRequest.of(0,10, sort));
        assertEquals(2, result.size());
        assertEquals(List.of(booking, booking2), result);

    }

    @Test
    void findByItemIdInAndEndIsBefore_whenEndNotBefore() {
        booking2.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking2);
        Collection<Booking> result =
                bookingRepository.findByItemIdInAndEndIsBefore(
                        itemsId,
                        LocalDateTime.now(),
                        PageRequest.of(0,10, sortAsc));

        assertEquals(List.of(booking), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByItemIdInAndEndIsAfterAndStartIsBefore() {
        booking.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking);

        Collection<Booking> result =
                bookingRepository.findByItemIdInAndEndIsAfterAndStartIsBefore(
                        itemsId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        PageRequest.of(0,4, sort));

        assertEquals(List.of(booking, booking3), result);
        assertEquals(2, result.size());
    }

    @Test
    void findByItemIdInAndStatusIs() {
        Collection<Booking> result =
                bookingRepository.findByItemIdInAndStatusIs(
                        itemsId,
                        BookingStatus.APPROVED,
                        PageRequest.of(0,4, sort));

        assertEquals(List.of(booking2), result);
        assertEquals(1, result.size());
    }

    @Test
    void findByItemIdInAndStatusIs_whenRejected() {
        Collection<Booking> result =
                bookingRepository.findByItemIdInAndStatusIs(
                        itemsId,
                        BookingStatus.REJECTED,
                        PageRequest.of(0,4, sort));

        assertEquals(List.of(), result);
        assertEquals(0, result.size());
    }

    @Test
    void findByItemIdInAndStatusIs_whenCanceled() {
        Collection<Booking> result =
                bookingRepository.findByItemIdInAndStatusIs(
                        itemsId,
                        BookingStatus.CANCELED,
                        PageRequest.of(0,4, sort));

        assertEquals(2, result.size());
        assertEquals(List.of(booking, booking3), result);
    }

    @Test
    void findByItemIdInAndStartIsAfter() {
        Collection<Booking> result =
                bookingRepository.findByItemIdInAndStartIsAfter(
                        itemsId,
                        LocalDateTime.now(),
                        PageRequest.of(0,4));

        assertEquals(0, result.size());
        assertEquals(List.of(), result);
    }

    @Test
    void findByItemIdInAndStartIsAfter_whenStartIsAfter() {
        booking2.setStart(LocalDateTime.now().plusDays(4));
        bookingRepository.save(booking2);

        Collection<Booking> result =
                bookingRepository.findByItemIdInAndStartIsAfter(
                        itemsId,
                        LocalDateTime.now(),
                        PageRequest.of(0,4));

        assertEquals(1, result.size());
        assertEquals(List.of(booking2), result);
    }

    @Test
    void existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs() {
        boolean result = bookingRepository.existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(
                        user.getId(),
                        item2.getId(),
                        LocalDateTime.now(),
                        BookingStatus.APPROVED);

        assertTrue(result);
    }

    @Test
    void existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs_Canceled() {
        boolean result = bookingRepository.existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(
                user.getId(),
                item2.getId(),
                LocalDateTime.now(),
                BookingStatus.CANCELED);

        assertFalse(result);
    }

    @Test
    void findFirstByItemIdAndStartIsAfterAndStatusIs() {
        booking2.setStart(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking2);

        Optional<Booking> result =
                bookingRepository.findFirstByItemIdAndStartIsAfterAndStatusIs(
                        item2.getId(),
                        LocalDateTime.now(),
                        BookingStatus.APPROVED,
                        sort);

        assertEquals(booking2, result.get());
    }

    @Test
    void findFirstByItemIdAndStartIsBeforeAndStatusIs() {
        Optional<Booking> result =
                bookingRepository.findFirstByItemIdAndStartIsBeforeAndStatusIs(
                        item.getId(),
                        LocalDateTime.now(),
                        BookingStatus.CANCELED,
                        sortAsc);

        assertEquals(booking, result.get());

    }
}