package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void getError() {
        String error = "error";
        ErrorResponse errorResponse = new ErrorResponse(error);

        assertEquals(error, errorResponse.getError());
    }
}