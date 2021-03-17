package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class AllPickedMoneyException extends BaseSprinklingException {

  public AllPickedMoneyException() {
    super(ErrorCode.ALL_PICKED_MONEY);
  }

}
