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

    boolean existsByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(Long userId,
                                                               Long itemId,
                                                               LocalDateTime now,
                                                               BookingStatus bookingStatus);

    Optional<Booking> findFirstByItemIdAndStartIsAfterAndStatusIs(Long itemId,
                                                                  LocalDateTime now,
                                                                  BookingStatus bookingStatus,
                                                                  Sort start);

    Optional<Booking> findFirstByItemIdAndStartIsBeforeAndStatusIs(Long itemId,
                                                                   LocalDateTime now,
                                                                   BookingStatus bookingStatus,
                                                                   Sort end);
}
