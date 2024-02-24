package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.validation.ValidationMarker;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
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
    private final String header = "X-Sharer-User-Id";

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    public ItemDto createItem(@RequestHeader(header) @Positive Long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Post Items {}, {}", userId, itemDto);
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @Validated(ValidationMarker.OnUpdate.class)
    public ItemDto updateItem(@RequestHeader(header) @Positive Long userId,
                              @Valid @RequestBody ItemDto itemDto,
                              @PathVariable @Positive Long itemId) {
        log.info("Patch Items/id {}, {}", itemId, itemDto);
        return itemService.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public OutgoingItemDto getItemById(@PathVariable @Positive Long itemId,
                                       @RequestHeader(header) @Positive Long userId) {
        log.info("Get Items/id {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public Collection<OutgoingItemDto> getItemsByUser(@RequestHeader(header) @Positive Long userId) {
        log.info("Get Items");
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        log.info("Get Items/search {}", text);
        return itemService.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    @Validated(ValidationMarker.OnCreate.class)
    public ItemCommentsDto createComments(@PathVariable @Positive Long itemId,
                                          @RequestHeader(header) @Positive Long userId,
                                          @Valid @RequestBody CommentDto comment) {
        log.info("Post Items/id/comment {}", itemId);
        return itemService.createComments(itemId, userId, comment);
    }
}