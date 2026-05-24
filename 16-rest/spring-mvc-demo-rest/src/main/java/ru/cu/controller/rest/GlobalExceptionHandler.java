package ru.cu.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.cu.dto.ErrorDto;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handeIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Error in controller", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("ER0001",
                "The request does not contain enough data"));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorDto> handeEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error("Error in controller", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("ER0002",
                "Entity not found"));
    }

}
