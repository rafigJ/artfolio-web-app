package ru.vsu.cs.artfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vsu.cs.artfolio.dto.RestExceptionDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {RestException.class})
    @ResponseBody
    public ResponseEntity<RestExceptionDto> handler(RestException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new RestExceptionDto(ex.getMessage()));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<RestExceptionDto> handler(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RestExceptionDto(ex.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseEntity<RestExceptionDto> handler(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new RestExceptionDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
