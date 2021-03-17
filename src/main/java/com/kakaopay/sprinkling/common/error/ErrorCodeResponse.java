package com.kakaopay.sprinkling.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorCodeResponse {

  private final String code;
  private final String message;

  @Builder
  public ErrorCodeResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ErrorCodeResponse of(ErrorCode errorCode) {
    return ErrorCodeResponse.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build();
  }

}
