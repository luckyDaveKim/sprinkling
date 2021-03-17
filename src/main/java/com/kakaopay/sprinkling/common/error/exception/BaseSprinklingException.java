package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class BaseSprinklingException extends RuntimeException {

  private final ErrorCode errorCode;

  public BaseSprinklingException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public BaseSprinklingException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public BaseSprinklingException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public BaseSprinklingException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }

}
