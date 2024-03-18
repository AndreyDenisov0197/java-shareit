package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Slf4j
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<UserDto> getUserById(long id) {
        return userMapper(get("/" + id));
    }

    public ResponseEntity<Object> getUsers() throws JsonProcessingException {
//        TypeReference<Collection<UserDto>> listType = new TypeReference<>() {};
//        return ResponseEntity.ok(mapper.readValue(get("").getBody().toString(), listType));
       return get("");
    }

    public ResponseEntity<UserDto> postUser(UserDto user) {
        return userMapper(post("", user));
    }

    public ResponseEntity<UserDto> updateUser(long id, UserDto user) {
        return userMapper(patch("/" + id, user));
    }

    public ResponseEntity<UserDto> deleteUser(long id) {
        return userMapper(delete("/" + id));
    }

    private ResponseEntity<UserDto> userMapper(ResponseEntity<Object> response) {
        if (response.getStatusCodeValue() == 200) {
            Object object = response.getBody();
            return ResponseEntity.ok(mapper.convertValue(object, UserDto.class));
        }
        return new ResponseEntity<>(new UserDto(), response.getStatusCode());
    }
}
