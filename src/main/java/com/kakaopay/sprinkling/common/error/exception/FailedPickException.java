package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class FailedPickException extends BaseSprinklingException {

  public FailedPickException() {
    super(ErrorCode.FAILED_PICK);
  }

  public FailedPickException(Throwable cause) {
    super(ErrorCode.FAILED_PICK, cause);
  }

}
