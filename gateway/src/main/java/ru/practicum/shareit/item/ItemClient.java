package ru.practicum.shareit.item;

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
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;

import java.util.Map;

@Slf4j
@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ResponseEntity<ItemDto> createItem(long userId, ItemDto itemDto) {
        ResponseEntity<Object> response = post("", userId, itemDto);

        if (response.getStatusCodeValue() == 200) {
            Object object = response.getBody();
            return ResponseEntity.ok(mapper.convertValue(object, ItemDto.class));
        }
        return new ResponseEntity<>(new ItemDto(), response.getStatusCode());
    }

    public ResponseEntity<ItemDto> updateItem(long userId, ItemDto itemDto, long itemId) {
        ResponseEntity<Object> response = patch("/" + itemId, userId, itemDto);

        if (response.getStatusCodeValue() == 200) {
            Object object = response.getBody();
            return ResponseEntity.ok(mapper.convertValue(object, ItemDto.class));
        }
        return new ResponseEntity<>(new ItemDto(), response.getStatusCode());
    }

    public ResponseEntity<OutgoingItemDto> getItemById(long itemId, long userId) {
        ResponseEntity<Object> response = get("/" + itemId, userId);

        if (response.getStatusCodeValue() == 200) {
            Object object = response.getBody();
            return ResponseEntity.ok(mapper.convertValue(object, OutgoingItemDto.class));
        }
        return new ResponseEntity<>(new OutgoingItemDto(), response.getStatusCode());
    }

    public ResponseEntity<Object> getItemsByUser(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchItem(String text, long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<ItemCommentsDto> createComments(long itemId, long userId, CommentDto comment) {
        ResponseEntity<Object> response = post("/" + itemId + "/comment", userId, comment);

        if (response.getStatusCodeValue() == 200) {
            Object object = response.getBody();
            return ResponseEntity.ok(mapper.convertValue(object, ItemCommentsDto.class));
        }
        return new ResponseEntity<>(new ItemCommentsDto(), response.getStatusCode());
    }
}
