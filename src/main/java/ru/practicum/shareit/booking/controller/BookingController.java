package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingServiceDb;
import ru.practicum.shareit.validation.BookingStateValidation;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingServiceDb bookingService;
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    BookingDto bookingRequest(@RequestBody @Validated(ValidationMarker.OnCreate.class)
                              BookingRestDto booking,
                              @RequestHeader(header) @Positive Long userId) {
        log.info("POST {}, {}", userId, booking);
        return bookingService.bookingRequest(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    BookingDto bookingConfirmation(@PathVariable Long bookingId,
                                   @RequestHeader(header) Long userId,
                                   @RequestParam Boolean approved) {
        log.info("PATCH/bookingId-{}, {}, {}", bookingId, userId, approved);
        return bookingService.bookingConfirmation(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBooking(@PathVariable Long bookingId,
                          @RequestHeader(header) Long userId) {
        log.info("GET/bookingId-{}, {}", bookingId, userId);
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    Collection<BookingDto> getAllBookingForBooker(@RequestParam(defaultValue = "ALL")
                                                  @BookingStateValidation String state,
                                                  @RequestHeader(header) Long userId) {
        log.info("GET ?state={}, {}", state, userId);
        return bookingService.getAllBookingForBooker(BookingState.valueOf(state), userId);
    }

    @GetMapping("/owner")
    Collection<BookingDto> getAllBookingForUserItems(@RequestParam(defaultValue = "ALL")
                                                     @BookingStateValidation String state,
                                                     @RequestHeader(header) Long userId) {
        log.info("GET /owner?state={}, {}", state, userId);
        return bookingService.getAllBookingForUserItems(BookingState.valueOf(state), userId);
    }


}
