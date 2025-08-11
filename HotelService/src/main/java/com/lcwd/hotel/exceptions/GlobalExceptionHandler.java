package com.lcwd.hotel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lcwd.hotel.payload.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)	
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exp){
		String message = exp.getMessage();
		ApiResponse apiResponse = ApiResponse.builder().
				message(message).success(true).status(HttpStatus.NOT_FOUND).build();
		
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
}
