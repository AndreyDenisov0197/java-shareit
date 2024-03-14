package ru.practicum.shareit.exeption;

public class NotAvailableItemException  extends RuntimeException {

    public NotAvailableItemException(String message) {
        super(message);
    }
}
