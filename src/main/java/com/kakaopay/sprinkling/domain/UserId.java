package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidUserIdException;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.servlet.http.HttpServletRequest;

@Getter
public class UserId extends SingleValue<Long> {

  private static final String USER_ID_HEADER = "X-USER-ID";

  public UserId(Long userId) {
    super(userId);
    this.validate(userId);
  }

  public static UserId of(HttpServletRequest req) {
    String userId = req.getHeader(USER_ID_HEADER);
    validate(userId);

    return new UserId(Long.parseLong(userId));
  }

  private static void validate(String userId) {
    if (Strings.isEmpty(userId)) {
      throw new InvalidUserIdException(userId);
    }

    try {
      Long.parseLong(userId);
    } catch (NumberFormatException e) {
      throw new InvalidUserIdException(e);
    }
  }

  private void validate(Long userId) {
    if (userId == null) {
      throw new InvalidUserIdException();
    }

    boolean isNegative = userId < 0;
    if (isNegative) {
      throw new InvalidUserIdException();
    }
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }

  @Converter
  public static class UserIdConverter implements AttributeConverter<UserId, Long> {

    @Override
    public Long convertToDatabaseColumn(UserId attribute) {
      return attribute == null ? null : attribute.getValue();
    }

    @Override
    public UserId convertToEntityAttribute(Long dbData) {
      return dbData == null ? null : new UserId(dbData);
    }

  }

}
