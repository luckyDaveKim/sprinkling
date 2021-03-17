package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class InvalidUserIdException extends BaseSprinklingException {

  public InvalidUserIdException() {
    super(ErrorCode.INVALID_USER_ID);
  }

  public InvalidUserIdException(String userId) {
    super(ErrorCode.INVALID_USER_ID, String.format("잘못된 사용자 식별값 '%s' 입니다.", userId));
  }

  public InvalidUserIdException(Throwable cause) {
    super(ErrorCode.INVALID_USER_ID, cause);
  }

}
