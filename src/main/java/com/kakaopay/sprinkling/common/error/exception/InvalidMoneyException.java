package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class InvalidMoneyException extends BaseSprinklingException {

  public InvalidMoneyException() {
    super(ErrorCode.INVALID_MONEY);
  }

  public InvalidMoneyException(String message) {
    super(ErrorCode.INVALID_MONEY, message);
  }

}
