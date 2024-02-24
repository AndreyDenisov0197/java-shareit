package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.model.BookingState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class BookingStateValidator implements ConstraintValidator<BookingStateValidation, String> {
    @Override
    public boolean isValid(String state, ConstraintValidatorContext context) {
        if (Arrays.stream(BookingState.values()).noneMatch((value) -> String.valueOf(value).equals(state))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Unknown state: UNSUPPORTED_STATUS")
                    .addPropertyNode("state")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
