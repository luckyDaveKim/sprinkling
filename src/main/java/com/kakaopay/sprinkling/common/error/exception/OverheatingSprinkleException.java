package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class OverheatingSprinkleException extends BaseSprinklingException {

  public OverheatingSprinkleException() {
    super(ErrorCode.OVERHEATING_SPRINKLE);
  }

}
