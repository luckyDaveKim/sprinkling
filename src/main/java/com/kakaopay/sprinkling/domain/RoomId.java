package com.kakaopay.sprinkling.domain;

import com.kakaopay.sprinkling.common.error.exception.InvalidRoomIdException;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.servlet.http.HttpServletRequest;

@Getter
public class RoomId extends SingleValue<String> {

  private static final String ROOM_ID_HEADER = "X-ROOM-ID";

  public RoomId(String roomId) {
    super(roomId);
    this.validate(roomId);
  }

  public static RoomId of(HttpServletRequest req) {
    return new RoomId(req.getHeader(ROOM_ID_HEADER));
  }

  private void validate(String roomId) {
    if (Strings.isEmpty(roomId)) {
      throw new InvalidRoomIdException();
    }
  }

  @Override
  public String toString() {
    return this.value;
  }

  @Converter
  public static class RoomIdConverter implements AttributeConverter<RoomId, String> {

    @Override
    public String convertToDatabaseColumn(RoomId attribute) {
      return attribute == null ? null : attribute.getValue();
    }

    @Override
    public RoomId convertToEntityAttribute(String dbData) {
      return dbData == null ? null : new RoomId(dbData);
    }

  }

}
