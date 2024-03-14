package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByBookerId(Long userId,
                                       PageRequest pageRequest);

    Collection<Booking> findByBookerIdAndEndIsBefore(Long userId,
                                                     LocalDateTime now,
                                                     PageRequest pageRequest);

    Collection<Booking> findByBookerIdAndEndIsAfterAndStartIsBefore(Long userId,
                                                                    LocalDateTime after,
                                                                    LocalDateTime start,
                                                                    PageRequest pageRequest);

    Collection<Booking> findByBookerIdAndStatusIs(Long userId,
                                                  BookingStatus bookingStatus,
                                                  PageRequest pageRequest);


    Collection<Booking> findByBookerIdAndStartIsAfter(Long userId,
                                                      LocalDateTime now,
                                                      PageRequest pageRequest);

    Collection<Booking> findByItemIdIn(List<Long> itemsId,
                                       PageRequest pageRequest);

    Collection<Booking> findByItemIdInAndEndIsBefore(List<Long> itemsId,
                                                     LocalDateTime now,
                                                     PageRequest pageRequest);

    Collection<Booking> findByItemIdInAndEndIsAfterAndStartIsBefore(List<Long> itemsId,
                                                                    LocalDateTime after,
                                                                    LocalDateTime start,
                                                                    PageRequest pageRequest);

    Collection<Booking> findByItemIdInAndStatusIs(List<Long> itemsId,
                                                  BookingStatus bookingStatus,
                                                  PageRequest pageRequest);

    Collection<Booking> findByItemIdInAndStartIsAfter(List<Long> itemsId,
                                                      LocalDateTime now,
                                                      PageRequest pageRequest);

    boolean existsByBooker_IdAndItemIdAndEndIsBeforeAndStatusIs(Long userId,
                                                               Long itemId,
                                                               LocalDateTime now,
                                                               BookingStatus bookingStatus);

    Optional<Booking>  findFirstByItem_idAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking>  findFirstByItem_idAndStartAfterAndStatusOrderByStartAsc(Long itemId,
                                                                               LocalDateTime now,
                                                                               BookingStatus bookingStatus);

    Optional<Booking> findFirstByItem_idAndStartAfterAndStatusOrderByEndAsc(Long itemId,
                                                                  LocalDateTime now,
                                                                  BookingStatus bookingStatus);

    Optional<Booking> findFirstByItem_idAndStartBeforeOrderByStartAsc(Long itemId,
                                                                   LocalDateTime now);

    Collection<Booking> findByItem_id(Long itemId);

    Collection<Booking> findByItem_idAndEndAfter(Long itemId, LocalDateTime now);

    Collection<Booking> findByItem_idAndEndIsBefore(Long itemId, LocalDateTime now);

    Collection<Booking> findByItem_idAndStartIsBefore(Long itemId, LocalDateTime now);

    Collection<Booking> findByItemIdAndStartBefore(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime now, BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime now, BookingStatus bookingStatus);
}
