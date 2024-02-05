package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ItemRequestStorage {
    private final Map<Integer, ItemRequest> requestMap = new HashMap<>();
    private int index;

    public ItemRequest createRequest(ItemRequest itemRequest) {
        itemRequest.setId(index++);
        int id = itemRequest.getId();
        requestMap.put(id, itemRequest);
        log.info("Request добвален");
        return requestMap.get(id);
    }

    public ItemRequest getRequestById(int id) {
        if (!requestMap.containsKey(id)) {
            throw new ObjectNotFoundException("Такого запроса нет!");
        }
        log.info("Request получен");
        return requestMap.get(id);
    }

}
