package ru.cu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handeIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Error in controller", ex);
        return new ModelAndView("customError", "errorText",
                messageSource.getMessage("bad-request", null, LocaleContextHolder.getLocale()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ModelAndView handeEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error("Error in controller", ex);
        return new ModelAndView("customError", "errorText",
                messageSource.getMessage("entity-not-found", null, LocaleContextHolder.getLocale()));
    }

}
