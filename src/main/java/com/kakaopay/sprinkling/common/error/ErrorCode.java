package com.kakaopay.sprinkling.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  /* Common */
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C-001-001", "오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

  /* Api */
  OVERHEATING_SPRINKLE(HttpStatus.SERVICE_UNAVAILABLE, "A-001-001", "돈을 너무 많이 뿌리고 있습니다. 잠시 후 다시 시도해주세요."),
  FAILED_PICK(HttpStatus.BAD_REQUEST, "A-001-002", "돈 줍기를 실패했습니다."),
  FAILED_CHECK(HttpStatus.BAD_REQUEST, "A-001-003", "뿌리기 조회를 실패했습니다."),

  /* Domain */
  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "D-001-001", "잘못된 토큰입니다."),

  INVALID_MONEY(HttpStatus.BAD_REQUEST, "D-002-001", "잘못된 금액입니다."),
  TOO_LARGE_MONEY(HttpStatus.PAYLOAD_TOO_LARGE, "D-002-002", "금액이 너무 큽니다."),

  INVALID_PICKER_COUNT(HttpStatus.BAD_REQUEST, "D-003-001", "잘못된 사용자 수 입니다."),
  TOO_LARGE_PICKER_COUNT(HttpStatus.PAYLOAD_TOO_LARGE, "D-003-002", "사용자 수가 너무 큽니다."),

  INVALID_USER_ID(HttpStatus.UNAUTHORIZED, "D-004-001", "잘못된 사용자 식별값입니다."),

  INVALID_ROOM_ID(HttpStatus.UNAUTHORIZED, "D-005-001", "잘못된 대화방 식별값입니다."),

  /* Entity */
  SPRINKLED_MONEY_NOT_FOUND(HttpStatus.NOT_FOUND, "E-001-001", "돈 뿌리기 정보를 찾을 수 없습니다."),

  ALL_PICKED_MONEY(HttpStatus.BAD_REQUEST, "E-002-001", "모든 돈을 다 주웠습니다."),
  CREATOR_CANNOT_PICK_MONEY(HttpStatus.FORBIDDEN, "E-002-002", "자신이 뿌린돈은 주울 수 없습니다."),
  EXPIRED_PICK_MONEY(HttpStatus.FORBIDDEN, "E-002-003", "줍기가 마감된 돈뿌리기 입니다."),
  ALREADY_PARTICIPATED_PICK_MONEY(HttpStatus.NOT_ACCEPTABLE, "E-002-004", "이미 돈뿌리기에 참가했습니다."),

  EXPIRED_CHECK_MONEY(HttpStatus.FORBIDDEN, "E-003-001", "조회가 마감된 돈뿌리기 입니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus httpStatus, String code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

}
