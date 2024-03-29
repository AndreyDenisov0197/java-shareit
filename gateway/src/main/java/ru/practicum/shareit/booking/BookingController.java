package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;
	protected static final String HEADER = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(HEADER) long userId,
											  @RequestParam(name = "state", defaultValue = "all") String stateParam,
											  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<BookingDto> bookItem(@RequestHeader(HEADER) long userId,
			@RequestBody @Validated(ValidationMarker.OnCreate.class) BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingDto> getBooking(@RequestHeader(HEADER) long userId,
			@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<BookingDto> bookingConfirmation(@RequestHeader(HEADER) long userId,
													   @PathVariable Long bookingId,
													   @RequestParam Boolean approved) {
		log.info("Patch booking {}, userId={}, approved={}", bookingId, userId, approved);
		return bookingClient.bookingConfirmation(userId, bookingId, approved);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingForUserItems(@RequestHeader(HEADER) long userId,
															@RequestParam(name = "state", defaultValue = "all") String stateParam,
															@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
															@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking owner with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getAllBookingForUserItems(state, userId, from, size);
	}



}
