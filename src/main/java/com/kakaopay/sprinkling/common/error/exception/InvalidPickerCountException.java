package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class InvalidPickerCountException extends BaseSprinklingException {

  public InvalidPickerCountException() {
    super(ErrorCode.INVALID_PICKER_COUNT);
  }

  public InvalidPickerCountException(String message) {
    super(ErrorCode.INVALID_PICKER_COUNT, message);
  }

}
