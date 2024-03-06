package ru.practicum.shareit.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingRestDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingDateTimeValidatorTest {
    @Mock
    private ConstraintValidatorContext context;
    @InjectMocks
    private BookingDateTimeValidator validator;
    private BookingRestDto booking;

    @BeforeEach
    public void beforeEach() {
        booking = BookingRestDto.builder()
                .itemId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void isValid() {
        assertTrue(validator.isValid(booking, context));
    }

    @Test
    void isValid_whenStartBefore() {
        booking.setStart(LocalDateTime.now().minusDays(1));
        assertFalse(validator.isValid(booking, context));
    }
}