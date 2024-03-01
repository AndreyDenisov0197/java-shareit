package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingRestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateTimeValidator implements ConstraintValidator<BookingDateTimeValidation, BookingRestDto> {

    @Override
    public boolean isValid(BookingRestDto booking, ConstraintValidatorContext context) {

        if (null == booking.getStart()) {
            return false;
        }
        if (null == booking.getEnd()) {
            return false;
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            return false;
        }
        if (booking.getStart().equals(booking.getEnd())) {
            return false;
        }
        return true;
    }

}
