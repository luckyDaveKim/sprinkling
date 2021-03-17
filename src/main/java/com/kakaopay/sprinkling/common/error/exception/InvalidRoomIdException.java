package com.kakaopay.sprinkling.common.error.exception;

import com.kakaopay.sprinkling.common.error.ErrorCode;

public class InvalidRoomIdException extends BaseSprinklingException {

  public InvalidRoomIdException() {
    super(ErrorCode.INVALID_ROOM_ID);
  }

}
