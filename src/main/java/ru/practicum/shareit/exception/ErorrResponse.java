package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErorrResponse<T> extends ResponseEntity<T> {

    public ErorrResponse(T body, HttpStatus status) {
        super(body, status);
    }
}
