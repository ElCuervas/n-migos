package com.company.n_migos.controller;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ModelAndView handleExpiredJwtException(ExpiredJwtException ex) {
        return new ModelAndView("redirect:/");
    }
}
