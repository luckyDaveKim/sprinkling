package com.kakaopay.sprinkling.common.error;

import com.kakaopay.sprinkling.common.error.exception.BaseSprinklingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalApiExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleExceptions(Exception ex) {
    return new ResponseEntity<>(ErrorResponse.of(ErrorCode.SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return new ResponseEntity<>(ErrorResponse.of(errorMessage), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BaseSprinklingException.class)
  public ResponseEntity<ErrorCodeResponse> handleBaseSprinklingException(BaseSprinklingException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    return new ResponseEntity<>(ErrorCodeResponse.of(errorCode), errorCode.getHttpStatus());
  }

}
