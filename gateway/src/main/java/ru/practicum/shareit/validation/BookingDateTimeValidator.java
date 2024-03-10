package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateTimeValidator implements ConstraintValidator<BookingDateTimeValidation, BookItemRequestDto> {

    @Override
    public boolean isValid(BookItemRequestDto booking, ConstraintValidatorContext context) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }

}