package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.repository.RequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestService requestService;
    protected static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    ResponseEntity<RequestDto> createRequest(@RequestBody RequestDto request,
                                             @RequestHeader(HEADER) Long userId) {
        log.info("POST /requests: {}", request);
        return ResponseEntity.ok(requestService.createRequest(request, userId));
    }

    @GetMapping
    ResponseEntity<Collection<RequestWithItemsDto>> getRequestsByUserId(@RequestHeader(HEADER) Long userId) {
        log.info("GET /requests {}", userId);
        return ResponseEntity.ok(requestService.getRequestsByUserId(userId));
    }

    @GetMapping("/all")
    ResponseEntity<Collection<RequestWithItemsDto>> getAllRequest(@RequestHeader(HEADER) Long userId,
                                                                  @RequestParam(defaultValue = "0") int from,
                                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("GET /requests/all?from={}&size={}, userId={}", from, size, userId);
        return ResponseEntity.ok(requestService.getAllRequest(userId, from, size));
    }

    @GetMapping("/{requestId}")
    ResponseEntity<RequestWithItemsDto> getRequestById(@PathVariable Long requestId,
                                                       @RequestHeader(HEADER) Long userId) {
        log.info("GET /requests/{}, {}", requestId, userId);
        return ResponseEntity.ok(requestService.getRequestById(requestId, userId));
    }
}