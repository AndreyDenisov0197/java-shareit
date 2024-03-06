package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class NotAvailableItemException extends RuntimeException {
    private final String message;

    public NotAvailableItemException(String message) {
        super(message);
        this.message = message;
    }
}
