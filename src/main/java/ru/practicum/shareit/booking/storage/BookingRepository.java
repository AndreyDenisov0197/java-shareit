package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findByBookerId(Long userId, Sort sort);

    Collection<Booking> findByBookerIdAndEndIsBefore(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findByBookerIdAndEndIsAfterAndStartIsBefore(Long userId,
                                                                    LocalDateTime after,
                                                                    LocalDateTime start,
                                                                    Sort sort);

    Collection<Booking> findByBookerIdAndStatusIs(Long userId, BookingStatus bookingStatus, Sort sort);


    Collection<Booking> findByBookerIdAndStartIsAfter(Long userId, LocalDateTime now, Sort sort);

    Collection<Booking> findByItemIdIn(List<Long> itemsId, Sort sort);

    Collection<Booking> findByItemIdInAndEndIsBefore(List<Long> itemsId, LocalDateTime now, Sort sort);

    Collection<Booking> findByItemIdInAndEndIsAfterAndStartIsBefore(List<Long> itemsId,
                                                                    LocalDateTime after,
                                                                    LocalDateTime start,
                                                                    Sort sort);

    Collection<Booking> findByItemIdInAndStatusIs(List<Long> itemsId, BookingStatus bookingStatus, Sort sort);

    Collection<Booking> findByItemIdInAndStartIsAfter(List<Long> itemsId, LocalDateTime now, Sort sort);

    boolean existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(Long userId, Long itemId, LocalDateTime now, BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndStartIsAfterAndStatusIs(Long itemId, LocalDateTime now, BookingStatus bookingStatus, Sort start);

    Optional<Booking> findFirstByItemIdAndStartIsBeforeAndStatusIs(Long itemId, LocalDateTime now, BookingStatus bookingStatus, Sort end);
}
