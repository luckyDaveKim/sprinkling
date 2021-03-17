package com.kakaopay.sprinkling.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private final String message;

  @Builder
  public ErrorResponse(String message) {
    this.message = message;
  }

  public static ErrorResponse of(String message) {
    return ErrorResponse.builder()
            .message(message)
            .build();
  }

}
