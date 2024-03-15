package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    protected static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader(HEADER) Long userId,
                                              @RequestBody ItemDto itemDto) {
        log.info("Post Items {}, {}", userId, itemDto);
        return ResponseEntity.ok(itemService.createItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(HEADER) Long userId,
                                              @RequestBody ItemDto itemDto,
                                              @PathVariable  Long itemId) {
        log.info("Patch Items/id {}, {}", itemId, itemDto);
        return ResponseEntity.ok(itemService.updateItem(userId, itemDto, itemId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OutgoingItemDto> getItemById(@PathVariable Long itemId,
                                                       @RequestHeader(HEADER) Long userId) {
        log.info("Get Items/id {}", itemId);
        return ResponseEntity.ok(itemService.getItemById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<Collection<OutgoingItemDto>> getItemsByUser(@RequestHeader(HEADER) Long userId,
                                                                      @RequestParam(defaultValue = "0") int from,
                                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Get Items");
        return ResponseEntity.ok(itemService.getItemsByUser(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<ItemDto>> searchItem(@RequestParam String text,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Get Items/search {}", text);
        return ResponseEntity.ok(itemService.searchItem(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<ItemCommentsDto> createComments(@PathVariable Long itemId,
                                                          @RequestHeader(HEADER) Long userId,
                                                          @RequestBody CommentDto comment) {
        log.info("Post Items/id/comment {}", itemId);
        return ResponseEntity.ok(itemService.createComments(itemId, userId, comment));
    }
}