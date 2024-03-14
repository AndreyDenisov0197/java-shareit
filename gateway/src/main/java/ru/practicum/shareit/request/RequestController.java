package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestClient requestClient;
    protected static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    ResponseEntity<Object> createRequest(@RequestBody @Validated(ValidationMarker.OnCreate.class) RequestDto request,
                                             @RequestHeader(HEADER) @Positive Long userId) {
        log.info("POST /requests: {}", request);
        return requestClient.createRequest(request, userId);
    }

    @GetMapping
    ResponseEntity<Object> getRequestsByUserId(@RequestHeader(HEADER) @Positive Long userId) {
        log.info("GET /requests {}", userId);
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getAllRequest(@RequestHeader(HEADER) @Positive Long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET /requests/all?from={}&size={}, userId={}", from, size, userId);
        return requestClient.getAllRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getRequestById(@PathVariable @Positive Long requestId,
                                                       @RequestHeader(HEADER) @Positive Long userId) {
        log.info("GET /requests/{}, {}", requestId, userId);
        return requestClient.getRequestById(requestId, userId);
    }
}
