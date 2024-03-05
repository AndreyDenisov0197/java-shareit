package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.ValidationMarker;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;
    protected static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader(HEADER) @Positive Long userId,
                                              @Validated(ValidationMarker.OnCreate.class)
                                              @RequestBody ItemDto itemDto) {
        log.info("Post Items {}, {}", userId, itemDto);
        return ResponseEntity.ok(itemService.createItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(HEADER) @Positive Long userId,
                                              @Validated(ValidationMarker.OnUpdate.class)
                                              @RequestBody ItemDto itemDto,
                              @PathVariable @Positive Long itemId) {
        log.info("Patch Items/id {}, {}", itemId, itemDto);
        return ResponseEntity.ok(itemService.updateItem(userId, itemDto, itemId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OutgoingItemDto> getItemById(@PathVariable @Positive Long itemId,
                                       @RequestHeader(HEADER) @Positive Long userId) {
        log.info("Get Items/id {}", itemId);
        return ResponseEntity.ok(itemService.getItemById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<Collection<OutgoingItemDto>> getItemsByUser(@RequestHeader(HEADER) @Positive Long userId,
                                                      @RequestParam(required = false, defaultValue = "0")
                                                      @Min(0) int from,
                                                      @RequestParam(required = false, defaultValue = "10")
                                                      @Positive int size) {
        log.info("Get Items");
        return ResponseEntity.ok(itemService.getItemsByUser(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchItem(@RequestParam String text,
                                          @RequestParam(required = false, defaultValue = "0")
                                          @Min(0) int from,
                                          @RequestParam(required = false, defaultValue = "10")
                                          @Positive int size) {
        log.info("Get Items/search {}", text);
        return ResponseEntity.ok(itemService.searchItem(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<ItemCommentsDto> createComments(@PathVariable @Positive Long itemId,
                                          @RequestHeader(HEADER) @Positive Long userId,
                                          @Validated(ValidationMarker.OnCreate.class)
                                          @RequestBody CommentDto comment) {
        log.info("Post Items/id/comment {}", itemId);
        return ResponseEntity.ok(itemService.createComments(itemId, userId, comment));
    }
}