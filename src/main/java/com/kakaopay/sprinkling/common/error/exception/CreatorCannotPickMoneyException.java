package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class CreatorCannotPickMoneyException extends BaseSprinklingException {

  public CreatorCannotPickMoneyException() {
    super(ErrorCode.CREATOR_CANNOT_PICK_MONEY);
  }

}
