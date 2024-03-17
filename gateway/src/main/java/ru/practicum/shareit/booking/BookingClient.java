package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Slf4j
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }


    public ResponseEntity<BookingDto> bookItem(long userId, BookItemRequestDto requestDto) {
        return bookingMapper(post("", userId, requestDto));
    }

    public ResponseEntity<BookingDto> getBooking(long userId, Long bookingId) {
        return bookingMapper(get("/" + bookingId, userId));
    }

    public ResponseEntity<BookingDto> bookingConfirmation(long userId, Long bookingId, Boolean approved) {
        return bookingMapper(patch("/" + bookingId + "?approved=" + approved, userId));
    }

    public ResponseEntity<Object> getAllBookingForUserItems(BookingState state, long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );

        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    private ResponseEntity<BookingDto> bookingMapper(ResponseEntity<Object> response) {
        if (response.getStatusCodeValue() == 200) {
            Object object = response.getBody();
            return ResponseEntity.ok(mapper.convertValue(object, BookingDto.class));
        }
        return new ResponseEntity<>(new BookingDto(), response.getStatusCode());
    }
}
