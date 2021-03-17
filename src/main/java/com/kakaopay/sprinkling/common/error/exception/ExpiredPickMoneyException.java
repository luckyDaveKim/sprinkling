package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class ExpiredPickMoneyException extends BaseSprinklingException {

  public ExpiredPickMoneyException() {
    super(ErrorCode.EXPIRED_PICK_MONEY);
  }

}
