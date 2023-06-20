package com.cudotiem.postservice.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.cudotiem.postservice.exception.TokenExpiredException;

@RestControllerAdvice
public class TokenControllerAdvice {

	@ExceptionHandler(value = TokenExpiredException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ErrorMessage handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {
		return new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(), ex.getMessage(), request.getDescription(false));
	}
}
