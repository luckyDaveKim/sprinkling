package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class ExpiredCheckMoneyException extends BaseSprinklingException {

  public ExpiredCheckMoneyException() {
    super(ErrorCode.EXPIRED_CHECK_MONEY);
  }

}
