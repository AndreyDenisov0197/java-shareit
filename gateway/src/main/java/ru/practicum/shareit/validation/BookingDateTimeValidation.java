package ru.practicum.shareit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingDateTimeValidator.class)
@Documented
public @interface BookingDateTimeValidation {

    String message() default "Booking validation failed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
