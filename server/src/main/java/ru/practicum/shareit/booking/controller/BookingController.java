package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    protected static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    ResponseEntity<BookingDto> bookingRequest(@RequestBody BookingRestDto booking,
                                              @RequestHeader(HEADER) Long userId) {
        log.info("POST {}, {}", userId, booking);
        return ResponseEntity.ok(bookingService.bookingRequest(booking, userId));
    }

    @PatchMapping("/{bookingId}")
    ResponseEntity<BookingDto> bookingConfirmation(@PathVariable Long bookingId,
                                                   @RequestHeader(HEADER) Long userId,
                                                   @RequestParam Boolean approved) {
        log.info("PATCH/bookingId-{}, {}, {}", bookingId, userId, approved);
        return ResponseEntity.ok(bookingService.bookingConfirmation(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<BookingDto> getBooking(@PathVariable Long bookingId,
                                          @RequestHeader(HEADER) Long userId) {
        log.info("GET/bookingId-{}, {}", bookingId, userId);
        return ResponseEntity.ok(bookingService.getBooking(bookingId, userId));
    }

    @GetMapping
    ResponseEntity<Collection<BookingDto>> getAllBookingForBooker(@RequestParam(defaultValue = "ALL") String state,
                                                                  @RequestHeader(HEADER) Long userId,
                                                                  @RequestParam(defaultValue = "0") int from,
                                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("GET ?state={}, {}", state, userId);
        return ResponseEntity.ok(bookingService.getAllBookingForBooker(BookingState.valueOf(state), userId, from, size));
    }

    @GetMapping("/owner")
    ResponseEntity<Collection<BookingDto>> getAllBookingForUserItems(@RequestParam(defaultValue = "ALL") String state,
                                                                     @RequestHeader(HEADER) Long userId,
                                                                     @RequestParam(defaultValue = "0") int from,
                                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("GET /owner?state={}, {}", state, userId);
        return ResponseEntity.ok(bookingService.getAllBookingForUserItems(BookingState.valueOf(state), userId, from, size));
    }
}