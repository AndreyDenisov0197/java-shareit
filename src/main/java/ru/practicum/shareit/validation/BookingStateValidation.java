package ru.practicum.shareit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingStateValidator.class)
@Documented
public @interface BookingStateValidation {

    String message() default "BookingState validation failed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
