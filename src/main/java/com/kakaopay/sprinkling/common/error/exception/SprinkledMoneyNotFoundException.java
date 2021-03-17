package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class SprinkledMoneyNotFoundException extends BaseSprinklingException {

  public SprinkledMoneyNotFoundException() {
    super(ErrorCode.SPRINKLED_MONEY_NOT_FOUND);
  }

}
