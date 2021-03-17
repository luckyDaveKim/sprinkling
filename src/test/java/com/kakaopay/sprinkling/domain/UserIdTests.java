package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidUserIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserIdTests {

  @Test
  @DisplayName("사용자 생성")
  public void shouldCreateUserId() {
    /* Given */
    long userIdNumber = 123;

    /* When */
    UserId userId = new UserId(userIdNumber);

    /* Then */
    Assertions.assertEquals(123, userId.getValue());
  }

  @Test
  @DisplayName("사용자 식별값은 음수일 수 없음")
  public void shouldThrowInvalidNegativeUserId() {
    /* Given */
    long userIdNumber = -123;

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidUserIdException.class, () -> new UserId(userIdNumber));
  }

  @Test
  @DisplayName("사용자 식별값은 null일 수 없음")
  public void shouldThrowNullUserId() {
    /* Given */
    Long userIdNumber = null;

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidUserIdException.class, () -> new UserId(userIdNumber));
  }

}
