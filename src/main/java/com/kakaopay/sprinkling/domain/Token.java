package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidTokenException;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.SecureRandom;
import java.util.Random;

@Getter
public class Token extends SingleValue<String> {

  public static final int TOKEN_LENGTH = 3;
  private static final Random RANDOM = new SecureRandom();
  private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

  public Token(String token) {
    super(token);
    this.validate(token);
  }

  private void validate(String token) {
    if (token == null) {
      throw new InvalidTokenException();
    }

    boolean isValidLength = token.length() == TOKEN_LENGTH;
    if (!isValidLength) {
      throw new InvalidTokenException();
    }

    if (!this.isValidChars(token)) {
      throw new InvalidTokenException();
    }
  }

  private boolean isValidChars(String token) {
    for (char c : token.toCharArray()) {
      if (CHARS.indexOf(c) < 0) {
        return false;
      }
    }

    return true;
  }

  public static Token generate() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < TOKEN_LENGTH; i++) {
      int randomIndex = RANDOM.nextInt(CHARS.length());
      sb.append(CHARS.charAt(randomIndex));
    }

    return new Token(sb.toString());
  }

  @Override
  public String toString() {
    return this.value;
  }

  @Converter
  public static class TokenConverter implements AttributeConverter<Token, String> {

    @Override
    public String convertToDatabaseColumn(Token attribute) {
      return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Token convertToEntityAttribute(String dbData) {
      return dbData == null ? null : new Token(dbData);
    }

  }

}
