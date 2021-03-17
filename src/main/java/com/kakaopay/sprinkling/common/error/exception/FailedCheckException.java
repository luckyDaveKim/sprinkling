package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class FailedCheckException extends BaseSprinklingException {

  public FailedCheckException() {
    super(ErrorCode.FAILED_CHECK);
  }

  public FailedCheckException(Throwable cause) {
    super(ErrorCode.FAILED_CHECK, cause);
  }

}
