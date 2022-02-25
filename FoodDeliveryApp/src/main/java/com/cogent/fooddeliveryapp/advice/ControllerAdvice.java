package com.cogent.fooddeliveryapp.advice;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cogent.fooddeliveryapp.exception.NameAlreadyExistsException;
import com.cogent.fooddeliveryapp.exception.NoDataFoundException;
import com.cogent.fooddeliveryapp.exception.apierror.ApiError;



@org.springframework.web.bind.annotation.ControllerAdvice 
//will handle all exceptions thrown by the controller or restcontroller
//using throws
public class ControllerAdvice extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<?> noDataFoundException(NoDataFoundException e)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "no data found");
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getMessage(), e);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(NameAlreadyExistsException.class)
	public ResponseEntity<?> nameAlreadyExistsException(NameAlreadyExistsException e)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", "name already exists");
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e)
	{
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
	/*
	 * @ExceptionHandler(Exception.class) public ResponseEntity<?>
	 * handleException(Exception e) { ApiError apiError = new
	 * ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
	 * apiError.setMessage(e.getMessage()); return buildResponseEntity(apiError); }
	 */
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		ApiError apiError = new ApiError(status);
		apiError.setMessage("validation error");
		apiError.addValidationErrors(ex.getFieldErrors());
		apiError.addValidationObjectErrors(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(apiError);
	}
	
	
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<Object>(apiError, apiError.getStatus());
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<?> handleMethodTypeMismatch(MethodArgumentTypeMismatchException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(e.getMessage());
		return buildResponseEntity(apiError);
	}
	
}
