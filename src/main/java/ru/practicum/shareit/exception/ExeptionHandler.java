package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExeptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ErorrResponse<String> notFoundHandle(Exception e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErorrResponse<>(e.getClass().getSimpleName() + " : " + e.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> constraintViolationExceptionhandle(ConstraintViolationException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return Map.of("error", e.getConstraintViolations().stream().findFirst().get().getMessage());
    }

    @ExceptionHandler({NotAvailableItemException.class,
            MethodArgumentNotValidException.class,
            ValidationException.class})
    public ErorrResponse<String> exceptionHandle(Exception e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErorrResponse<>(e.getClass().getSimpleName() + " : " + e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ErorrResponse<String> throwableHandle(Throwable e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErorrResponse<>(e.getClass().getSimpleName() + " : " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
