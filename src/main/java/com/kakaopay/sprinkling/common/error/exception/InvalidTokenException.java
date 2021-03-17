package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class InvalidTokenException extends BaseSprinklingException {

  public InvalidTokenException() {
    super(ErrorCode.INVALID_TOKEN);
  }

}
