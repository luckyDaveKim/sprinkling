package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidTokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TokenTests {

  @Test
  @DisplayName("토큰 생성")
  public void shouldCreateToken() {
    /* Given */
    String tokenText = "aB3";

    /* When */
    Token token = new Token(tokenText);

    /* Then */
    Assertions.assertEquals(tokenText, token.getValue());
  }

  @Test
  @DisplayName("랜덤 토큰 생성")
  public void shouldCreateRandomToken() {
    /* Given */
    /* When */
    Token token = Token.generate();

    /* Then */
    Assertions.assertNotNull(token.getValue());
    Assertions.assertEquals(Token.TOKEN_LENGTH, token.getValue().length());
  }

  @Test
  @DisplayName("사용가능한 토큰 문자 종류는 제한되어 있음")
  public void shouldThrowInvalidTextToken() {
    /* Given */
    String tokenText = "aB<";

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidTokenException.class, () -> new Token(tokenText));
  }

  @Test
  @DisplayName("토큰의 길이는 3자리임")
  public void shouldThrowInvalidLengthToken() {
    /* Given */
    String shortTokenText = "aB";
    String longTokenText = "aBcD";

    /* When */
    /* Then */
    Assertions.assertThrows(InvalidTokenException.class, () -> new Token(shortTokenText));
    Assertions.assertThrows(InvalidTokenException.class, () -> new Token(longTokenText));
  }

}
