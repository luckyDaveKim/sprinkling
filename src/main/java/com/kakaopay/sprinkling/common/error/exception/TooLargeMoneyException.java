package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

import java.math.BigInteger;

public class TooLargeMoneyException extends BaseSprinklingException {

  public TooLargeMoneyException() {
    super(ErrorCode.TOO_LARGE_MONEY);
  }

  public TooLargeMoneyException(BigInteger money) {
    super(ErrorCode.TOO_LARGE_MONEY, String.format("금액 '%d'는 너무 큽니다.", money));
  }

}
