package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.validation.ValidationMarker;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;
    protected static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader(HEADER) @Positive long userId,
                                              @Validated(ValidationMarker.OnCreate.class)
                                              @RequestBody ItemDto itemDto) {
        log.info("Post Items {}, {}", userId, itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(HEADER) @Positive long userId,
                                              @Validated(ValidationMarker.OnUpdate.class)
                                              @RequestBody ItemDto itemDto,
                                              @PathVariable @Positive Long itemId) {
        log.info("Patch Items/id {}, {}", itemId, itemDto);
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OutgoingItemDto> getItemById(@PathVariable @Positive long itemId,
                                                       @RequestHeader(HEADER) @Positive Long userId) {
        log.info("Get Items/id {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader(HEADER) @Positive long userId,
                                                                      @RequestParam(defaultValue = "0")
                                                                      @Min(0) int from,
                                                                      @RequestParam(defaultValue = "10")
                                                                      @Positive int size) {
        log.info("Get Items");
        return itemClient.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @RequestHeader(HEADER) @Positive long userId,
                                             @RequestParam(defaultValue = "0")
                                                 @Min(0) int from,
                                             @RequestParam(defaultValue = "10")
                                                          @Positive int size) {
        log.info("Get Items/search {}", text);
        return itemClient.searchItem(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<ItemCommentsDto> createComments(@PathVariable @Positive Long itemId,
                                                          @RequestHeader(HEADER) @Positive Long userId,
                                                          @Validated(ValidationMarker.OnCreate.class)
                                                          @RequestBody CommentDto comment) {
        log.info("Post Items/id/comment {}", itemId);
        return itemClient.createComments(itemId, userId, comment);
    }
}