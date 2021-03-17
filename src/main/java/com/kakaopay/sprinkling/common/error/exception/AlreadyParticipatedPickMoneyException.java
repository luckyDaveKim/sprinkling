package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class AlreadyParticipatedPickMoneyException extends BaseSprinklingException {

  public AlreadyParticipatedPickMoneyException() {
    super(ErrorCode.ALREADY_PARTICIPATED_PICK_MONEY);
  }

}
