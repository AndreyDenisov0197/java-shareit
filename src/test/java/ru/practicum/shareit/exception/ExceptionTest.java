package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void getError() {
        String error = "error";
        ErrorResponse errorResponse = new ErrorResponse(error);

        assertEquals(error, errorResponse.getError());
    }

    @Test
    void getMessageNotAvailableItemException() {
        String message = "message";
        NotAvailableItemException notAvailableItemException  = new NotAvailableItemException(message);

        assertEquals(message, notAvailableItemException.getMessage());
    }

    @Test
    void getMessageNotFoundException() {
        String message = "message";
        NotFoundException notFoundException  = new NotFoundException(message);

        assertEquals(message, notFoundException.getMessage());
    }
}