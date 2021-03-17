package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class TooLargePickerCountException extends BaseSprinklingException {

  public TooLargePickerCountException() {
    super(ErrorCode.TOO_LARGE_PICKER_COUNT);
  }

  public TooLargePickerCountException(long pickerCount) {
    super(ErrorCode.TOO_LARGE_PICKER_COUNT, String.format("뿌릴 인원 '%d'은 너무 큽니다.", pickerCount));
  }

}
